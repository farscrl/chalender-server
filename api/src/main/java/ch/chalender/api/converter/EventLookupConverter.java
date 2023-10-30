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

        switch (event.getEventStatus()) {
            case PUBLISHED:
                addEventLookupsForAllOccurrences(eventLookups, event, event.getCurrentlyPublished());
                return eventLookups;
            case NEW_MODIFICATION:
                addEventLookupsForAllOccurrences(eventLookups, event, event.getWaitingForReview());
                return eventLookups;
            case DRAFT:
            case IN_REVIEW:
            case REJECTED:
            case INVALID:
            default:
                return eventLookups;
        }
    }

    private static void addEventLookupsForAllOccurrences(List<EventLookup> list, Event event, EventVersion version) {
        version.getOccurrences().forEach(occurrence -> {
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
            eventLookup.setEventId(event.getId());

            if (!event.getCurrentlyPublished().getImages().isEmpty()) {
                eventLookup.setImageUrl(event.getCurrentlyPublished().getImages().get(0).getUrl());
            }

            list.add(eventLookup);
        });
    }
}
