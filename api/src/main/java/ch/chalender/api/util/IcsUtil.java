package ch.chalender.api.util;

import ch.chalender.api.model.EventLookup;
import ch.chalender.api.model.EventOccurrence;
import ch.chalender.api.model.EventVersion;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IcsUtil {
    public static Calendar exportAllEventLookupsAsCalendar(List<EventLookup> eventsLookup) {
        List<VEvent> events = new ArrayList<>();

        for (EventLookup eventLookup : eventsLookup) {
            VEvent event = generateVEventFromEventLookup(eventLookup);
            events.add(event);
        }

        Calendar icsCalendar = new Calendar(new ComponentList<>(events));
        icsCalendar.add(new ProdId("-//chalender.ch//iCal4j 1.0//EN"));

        return icsCalendar;
    }

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

    public static VEvent generateVEventFromEventLookup(EventLookup eventLookup) {
        VEvent event = null;

        String eventSummary = eventLookup.getTitle();
        if (eventLookup.isCancelled()) {
            eventSummary = "[ANNULLÀ] " + eventSummary;
        }

        if (eventLookup.isAllDay()) {
            event = new VEvent(eventLookup.getDate(), eventSummary);
        } else if (eventLookup.getEnd() == null) {
            LocalDateTime start = LocalDateTime.of(eventLookup.getDate(), DataUtil.convertStringToLocalTime(eventLookup.getStart()));
            LocalDateTime end = start.plusHours(2);
            event = new VEvent(start, end, eventSummary);
        } else {
            LocalDateTime start = LocalDateTime.of(eventLookup.getDate(), DataUtil.convertStringToLocalTime(eventLookup.getStart()));
            LocalDateTime end = LocalDateTime.of(eventLookup.getDate(), DataUtil.convertStringToLocalTime(eventLookup.getEnd()));
            if (end.isBefore(start)) {
                end = end.plusDays(1);
            }
            event = new VEvent(start, end, eventSummary);

        }
        event.add(new Uid(eventLookup.getOccurrenceId()));

        Location location = new Location(eventLookup.getLocation());
        event.add(location);

        return event;
    }
}
