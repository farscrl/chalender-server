package ch.chalender.api.migration;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Migration to convert LocalTime fields (start, end) from BSON Date to String format.
 *
 * Problem: LocalTime was stored as BSON Date (full timestamp), which caused timezone
 * issues when the JVM timezone or DST changed. This migration converts the fields
 * to String format "HH:mm" which is timezone-agnostic.
 */
@ChangeUnit(id = "V001_MigrateLocalTimeToString", order = "001", author = "gion-andri")
public class V001_MigrateLocalTimeToString {

    private static final Logger logger = LoggerFactory.getLogger(V001_MigrateLocalTimeToString.class);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Execution
    public void execute(MongoTemplate mongoTemplate) {
        logger.info("Starting migration: Converting LocalTime fields to String format");

        long totalEvents = mongoTemplate.getCollection("events").countDocuments();
        logger.info("Found {} events to process", totalEvents);

        int processedCount = 0;
        int updatedCount = 0;

        for (Document event : mongoTemplate.getCollection("events").find()) {
            boolean updated = false;

            // Process all EventVersion fields that contain occurrences
            updated |= processEventVersion(event, "draft");
            updated |= processEventVersion(event, "currentlyPublished");
            updated |= processEventVersion(event, "waitingForReview");
            updated |= processEventVersion(event, "rejected");

            // Process versions array
            List<Document> versions = event.getList("versions", Document.class);
            if (versions != null) {
                for (Document version : versions) {
                    updated |= processOccurrences(version);
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

        logger.info("Migration complete: Processed {} events, updated {}", processedCount, updatedCount);
    }

    @RollbackExecution
    public void rollback(MongoTemplate mongoTemplate) {
        // Rollback is not implemented as converting back would require knowing
        // the original reference date, which is not stored.
        logger.warn("Rollback not implemented for this migration. Manual intervention required if needed.");
    }

    private boolean processEventVersion(Document event, String fieldName) {
        Document version = event.get(fieldName, Document.class);
        if (version == null) {
            return false;
        }
        return processOccurrences(version);
    }

    private boolean processOccurrences(Document version) {
        List<Document> occurrences = version.getList("occurrences", Document.class);
        if (occurrences == null || occurrences.isEmpty()) {
            return false;
        }

        boolean updated = false;
        for (Document occurrence : occurrences) {
            updated |= convertTimeField(occurrence, "start");
            updated |= convertTimeField(occurrence, "end");
        }
        return updated;
    }

    private boolean convertTimeField(Document occurrence, String fieldName) {
        Object value = occurrence.get(fieldName);

        if (value == null) {
            return false;
        }

        // Already a String - no conversion needed
        if (value instanceof String) {
            return false;
        }

        // Convert Date to String
        if (value instanceof Date date) {
            String timeString = convertDateToTimeString(date);
            occurrence.put(fieldName, timeString);
            return true;
        }

        logger.warn("Unexpected type for field {}: {}", fieldName, value.getClass().getName());
        return false;
    }

    private String convertDateToTimeString(Date date) {
        // Spring Data MongoDB converts BSON Date to LocalTime using the JVM's default timezone.
        // We must use the same timezone to preserve the times as they appear in the API.
        Instant instant = date.toInstant();
        LocalTime time = LocalTime.ofInstant(instant, ZoneId.systemDefault());
        return time.format(TIME_FORMATTER);
    }
}
