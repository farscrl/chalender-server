package ch.chalender.api.util;

import ch.chalender.api.model.EventOccurrence;
import ch.chalender.api.model.EventVersion;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Uid;

import java.time.LocalDateTime;

public class IcsUtil {
    public static VEvent generateVEventFromOccurrence(EventVersion version, EventOccurrence occurrence) {
        VEvent event = null;

        String eventSummary = version.getTitle();
        if (occurrence.isCancelled()) {
            eventSummary = "[ANNULLÀ] " + eventSummary;
        }

        if (occurrence.isAllDay()) {
            event = new VEvent(occurrence.getDate(), eventSummary);
        } else if (occurrence.getEnd() == null) {
            LocalDateTime start = LocalDateTime.of(occurrence.getDate(), occurrence.getStart());
            LocalDateTime end = start.plusHours(2);
            event = new VEvent(start, end, eventSummary);
        } else {
            LocalDateTime start = LocalDateTime.of(occurrence.getDate(), occurrence.getStart());
            LocalDateTime end = LocalDateTime.of(occurrence.getDate(), occurrence.getEnd());
            if (end.isBefore(start)) {
                end = end.plusDays(1);
            }
            event = new VEvent(start, end, eventSummary);

        }
        event.add(new Uid(occurrence.getOccurrenceUid()));

        Location location = new Location(version.getLocation());
        event.add(location);

        return event;
    }
}
