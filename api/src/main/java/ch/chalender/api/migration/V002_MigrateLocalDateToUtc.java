package ch.chalender.api.migration;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

/**
 * Migration to fix LocalDate timezone issues.
 *
 * Problem: LocalDate fields were stored inconsistently:
 * - Some with UTC (00:00:00Z) - correct
 * - Some with CET offset (23:00:00Z previous day) - needs +1h
 * - Some with CEST offset (22:00:00Z previous day) - needs +2h
 *
 * Solution: Detect the hour and adjust accordingly:
 * - 22:00 UTC → add 2 hours (was CEST, +2)
 * - 23:00 UTC → add 1 hour (was CET, +1)
 * - 00:00 UTC → no change (already correct)
 * - Other hours → log for manual review
 */
@ChangeUnit(id = "V002_MigrateLocalDateToUtc", order = "002", author = "gion-andri")
public class V002_MigrateLocalDateToUtc {

    private static final Logger logger = LoggerFactory.getLogger(V002_MigrateLocalDateToUtc.class);

    // Counters for statistics
    private int countAlreadyMidnight = 0;
    private int countCetOffset = 0;      // 23:00 → +1h
    private int countCestOffset = 0;     // 22:00 → +2h
    private int countUnexpectedHour = 0;

    @Execution
    public void execute(MongoTemplate mongoTemplate) {
        logger.info("Starting migration: Converting LocalDate fields to UTC midnight");

        long totalEvents = mongoTemplate.getCollection("events").countDocuments();
        logger.info("Found {} events to process", totalEvents);

        int processedCount = 0;
        int updatedCount = 0;

        for (Document event : mongoTemplate.getCollection("events").find()) {
            boolean updated = false;
            String eventId = event.getObjectId("_id").toString();

            // Convert top-level date fields
            updated |= convertDateFieldToUtc(event, "firstOccurrenceDate", eventId);
            updated |= convertDateFieldToUtc(event, "lastOccurrenceDate", eventId);

            // Process all EventVersion fields that contain occurrences
            updated |= processEventVersion(event, "draft", eventId);
            updated |= processEventVersion(event, "currentlyPublished", eventId);
            updated |= processEventVersion(event, "waitingForReview", eventId);
            updated |= processEventVersion(event, "rejected", eventId);

            // Process versions array
            List<Document> versions = event.getList("versions", Document.class);
            if (versions != null) {
                for (Document version : versions) {
                    updated |= processOccurrences(version, eventId);
                }
            }

            if (updated) {
                mongoTemplate.getCollection("events").replaceOne(
                        new Document("_id", event.get("_id")),
                        event
                );
                updatedCount++;
            }

            processedCount++;
            if (processedCount % 100 == 0) {
                logger.info("Processed {}/{} events, updated {}", processedCount, totalEvents, updatedCount);
            }
        }

        logger.info("=== Migration Statistics ===");
        logger.info("Total events processed: {}", processedCount);
        logger.info("Events updated: {}", updatedCount);
        logger.info("Date fields already at midnight UTC (no change): {}", countAlreadyMidnight);
        logger.info("Date fields with CET offset (23:00 → +1h): {}", countCetOffset);
        logger.info("Date fields with CEST offset (22:00 → +2h): {}", countCestOffset);
        logger.info("Date fields with unexpected hour (logged for review): {}", countUnexpectedHour);
        logger.info("=== Migration Complete ===");
    }

    @RollbackExecution
    public void rollback(MongoTemplate mongoTemplate) {
        logger.warn("Rollback not implemented for this migration. Manual intervention required if needed.");
    }

    private boolean processEventVersion(Document event, String fieldName, String eventId) {
        Document version = event.get(fieldName, Document.class);
        if (version == null) {
            return false;
        }
        return processOccurrences(version, eventId);
    }

    private boolean processOccurrences(Document version, String eventId) {
        List<Document> occurrences = version.getList("occurrences", Document.class);
        if (occurrences == null || occurrences.isEmpty()) {
            return false;
        }

        boolean updated = false;
        for (Document occurrence : occurrences) {
            updated |= convertDateFieldToUtc(occurrence, "date", eventId);
        }
        return updated;
    }

    private boolean convertDateFieldToUtc(Document document, String fieldName, String eventId) {
        Object value = document.get(fieldName);

        if (value == null) {
            return false;
        }

        if (!(value instanceof Date date)) {
            logger.warn("Event {}: Unexpected type for field {}: {}", eventId, fieldName, value.getClass().getName());
            return false;
        }

        // Get the hour in UTC
        ZonedDateTime utcDateTime = date.toInstant().atZone(ZoneOffset.UTC);
        int hour = utcDateTime.getHour();
        int minute = utcDateTime.getMinute();

        // Only process if minutes are 0 (expected for date-only fields)
        if (minute != 0) {
            logger.warn("Event {}: Field {} has unexpected minutes: {} (full: {})",
                    eventId, fieldName, minute, utcDateTime);
            countUnexpectedHour++;
            return false;
        }

        Date newDate;
        switch (hour) {
            case 0:
                // Already midnight UTC - no change needed
                countAlreadyMidnight++;
                return false;

            case 23:
                // CET offset: 23:00 previous day → add 1 hour to get midnight
                newDate = Date.from(date.toInstant().plusSeconds(3600));
                countCetOffset++;
                break;

            case 22:
                // CEST offset: 22:00 previous day → add 2 hours to get midnight
                newDate = Date.from(date.toInstant().plusSeconds(7200));
                countCestOffset++;
                break;

            default:
                // Unexpected hour - log for manual review
                logger.warn("Event {}: Field {} has unexpected hour: {} (full: {})",
                        eventId, fieldName, hour, utcDateTime);
                countUnexpectedHour++;
                return false;
        }

        document.put(fieldName, newDate);
        return true;
    }
}