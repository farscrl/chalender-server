package ch.chalender.api.service;

import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventFilter;

import java.util.List;

public interface EventsService {
    public List<Event> getPublicEvents(EventFilter filter);
    public Event createEvent(Event event);

    public Event getEvent(String id);
}
