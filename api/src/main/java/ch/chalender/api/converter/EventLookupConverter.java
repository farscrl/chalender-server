package ch.chalender.api.converter;

import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventLookup;
import ch.chalender.api.model.EventVersion;

import java.util.ArrayList;
import java.util.List;

public class EventLookupConverter {

    public static List<EventLookup> getEventLookups(Event event) {
        List<EventLookup> eventLookups = new ArrayList<>();

        if (event.getCurrentlyPublished() == null) {
            return eventLookups;
        }

        event.getCurrentlyPublished().getOccurrences().forEach(occurrence -> {
            EventVersion version = event.getCurrentlyPublished();
            EventLookup eventLookup = new EventLookup();
            eventLookup.setTitle(version.getTitle());
            eventLookup.setGenres(version.getGenres());
            eventLookup.setLocation(version.getLocation());
            eventLookup.setRegions(version.getRegions());
            eventLookup.setEventLanguages(version.getEventLanguages());
            eventLookup.setDate(occurrence.getDate());
            eventLookup.setStart(occurrence.getStart());
            eventLookup.setEnd(occurrence.getEnd());
            eventLookup.setAllDay(occurrence.isAllDay());
            eventLookup.setCancelled(occurrence.isCancelled());
            eventLookup.setEventId(event.getId().toString());

            if (!event.getCurrentlyPublished().getImages().isEmpty()) {
                eventLookup.setImageUrl(event.getCurrentlyPublished().getImages().get(0).getUrl());
            }

            eventLookups.add(eventLookup);
        });

        return eventLookups;
    }
}
