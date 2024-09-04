package ch.chalender.api.util;

import ch.chalender.api.model.EventLookup;
import ch.chalender.api.model.EventOccurrence;
import ch.chalender.api.model.EventVersion;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.model.property.immutable.ImmutableVersion;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

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
        icsCalendar.add(getTimeZone());

        return icsCalendar;
    }

    public static Calendar generateIcs(String id, EventVersion version, EventOccurrence occurrence) {
        VEvent event = IcsUtil.generateVEventFromOccurrence(id, version, occurrence);

        Calendar icsCalendar = new Calendar();
        icsCalendar.add(new ProdId("-//chalender.ch//iCal4j 1.0//EN"));
        icsCalendar.add(ImmutableVersion.VERSION_2_0);
        icsCalendar.add(getTimeZone());

        icsCalendar.add(event);

        return icsCalendar;
    }

    private static VEvent generateVEventFromOccurrence(String id, EventVersion version, EventOccurrence occurrence) {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Zurich");

        VEvent event = null;

        String eventSummary = version.getTitle();
        if (occurrence.isCancelled()) {
            eventSummary = "[ANNULLÀ] " + eventSummary;
        }

        if (occurrence.isAllDay()) {
            event = new VEvent(occurrence.getDate(), eventSummary);
        } else if (occurrence.getEnd() == null) {
            ZonedDateTime start = LocalDateTime.of(occurrence.getDate(), occurrence.getStart()).atZone(timeZone.toZoneId());
            ZonedDateTime end = start.plusHours(2);
            event = new VEvent(start, end, eventSummary);
        } else {
            ZonedDateTime start = LocalDateTime.of(occurrence.getDate(), occurrence.getStart()).atZone(timeZone.toZoneId());
            ZonedDateTime end = LocalDateTime.of(occurrence.getDate(), occurrence.getEnd()).atZone(timeZone.toZoneId());
            if (end.isBefore(start)) {
                end = end.plusDays(1);
            }
            event = new VEvent(start, end, eventSummary);

        }
        event.add(new Uid(occurrence.getOccurrenceUid()));
        event.add(getTimeZone());
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

    private static VEvent generateVEventFromEventLookup(EventLookup eventLookup) {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Zurich");

        VEvent event = null;

        String eventSummary = eventLookup.getTitle();
        if (eventLookup.isCancelled()) {
            eventSummary = "(ANNULLÀ) " + eventSummary;
        }

        if (eventLookup.isAllDay()) {
            event = new VEvent(eventLookup.getDate(), eventSummary);
        } else if (eventLookup.getEnd() == null) {
            ZonedDateTime start = LocalDateTime.of(eventLookup.getDate(), DataUtil.convertStringToLocalTime(eventLookup.getStart())).atZone(timeZone.toZoneId());
            ZonedDateTime end = start.plusHours(2);
            event = new VEvent(start, end, eventSummary);
        } else {
            ZonedDateTime start = LocalDateTime.of(eventLookup.getDate(), DataUtil.convertStringToLocalTime(eventLookup.getStart())).atZone(timeZone.toZoneId());
            ZonedDateTime end = LocalDateTime.of(eventLookup.getDate(), DataUtil.convertStringToLocalTime(eventLookup.getEnd())).atZone(timeZone.toZoneId());
            if (end.isBefore(start)) {
                end = end.plusDays(1);
            }
            event = new VEvent(start, end, eventSummary);

        }
        event.add(new Uid(eventLookup.getOccurrenceId()));
        event.add(getTimeZone());
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

    public static VTimeZone getTimeZone() {
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
        net.fortuna.ical4j.model.TimeZone timezone = registry.getTimeZone("Europe/Zurich");
        return timezone.getVTimeZone();
    }
}
