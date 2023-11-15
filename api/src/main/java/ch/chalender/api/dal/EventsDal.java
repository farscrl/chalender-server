package ch.chalender.api.dal;

import ch.chalender.api.model.Event;
import ch.chalender.api.model.ModerationEventsFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventsDal {
    public Page<Event> getAllEvents(ModerationEventsFilter filter, Pageable pageable);
    public Page<Event> getAllEventsByUser(ModerationEventsFilter filter, Pageable pageable, String email);
}
