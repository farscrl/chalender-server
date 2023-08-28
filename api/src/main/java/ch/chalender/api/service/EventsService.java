package ch.chalender.api.service;

import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventFilter;
import ch.chalender.api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventsService {
    public Event createEvent(Event event);
    public Event getEvent(String id);
    public Page<Event> listAllEvents(EventFilter filter, Pageable pageable);
    public Page<Event> listAllEventsByUser(User user, Pageable pageable);
    public Event acceptChanges(String id);
    public Event refuseChanges(String id);
}
