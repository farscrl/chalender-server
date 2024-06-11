package ch.chalender.api.util;

import ch.chalender.api.model.EventLookup;
import ch.chalender.api.model.EventOccurrence;
import ch.chalender.api.model.EventVersion;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.model.property.immutable.ImmutableVersion;

import java.net.URI;
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
        icsCalendar.add(ImmutableVersion.VERSION_2_0);

        return icsCalendar;
    }

    public static VEvent generateVEventFromOccurrence(String id, EventVersion version, EventOccurrence occurrence) {
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
        if (version.getDescription() != null && !version.getDescription().isBlank()) {
            event.add(new Description(version.getDescription()));
        }
        event.add(new Url(URI.create("https://chalender.ch/" + id)));
        if (occurrence.isCancelled()) {
            event.add(new Status(Status.VALUE_CANCELLED));
        } else {
            event.add(new Status(Status.VALUE_CONFIRMED));
        }
        if (version.getOrganiser() != null && !version.getOrganiser().isBlank()) {
            event.add(new Contact(version.getOrganiser()));
        }

        if (version.getLocation() != null && !version.getLocation().isBlank()) {
            event.add(new Location(version.getLocation()));
        }

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
        if (eventLookup.getDescription() != null && !eventLookup.getDescription().isBlank()) {
            event.add(new Description(eventLookup.getDescription()));
        }
        event.add(new Url(URI.create("https://chalender.ch/" + eventLookup.getEventId())));
        if (eventLookup.isCancelled()) {
            event.add(new Status(Status.VALUE_CANCELLED));
        } else {
            event.add(new Status(Status.VALUE_CONFIRMED));
        }
        if (eventLookup.getOrganiser() != null && !eventLookup.getOrganiser().isBlank()) {
            event.add(new Contact(eventLookup.getOrganiser()));
        }
        if (eventLookup.getLocation() != null && !eventLookup.getLocation().isBlank()) {
            event.add(new Location(eventLookup.getLocation()));
        }

        return event;
    }
}
