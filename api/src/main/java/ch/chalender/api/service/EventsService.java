package ch.chalender.api.service;

import ch.chalender.api.dto.ModerationComment;
import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventVersion;
import ch.chalender.api.model.ModerationEventsFilter;
import ch.chalender.api.model.User;
import jakarta.mail.MessagingException;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.UnsupportedEncodingException;

public interface EventsService {
    public Event createEvent(Event event);
    public Event updateEvent(Event event);
    public Event getEvent(String id);
    public Resource getEventIcs(String id, String uid);
    public Page<Event> listAllEvents(ModerationEventsFilter filter, Pageable pageable);
    public Page<Event> listAllEventsByUser(User user, Pageable pageable);
    public Event acceptChanges(String id, ModerationComment moderationComment) throws RuntimeException, MessagingException, UnsupportedEncodingException;
    public Event refuseChanges(String id, ModerationComment moderationComment) throws RuntimeException, MessagingException, UnsupportedEncodingException;
    public Event changeAndPublish(String id, EventVersion eventVersion) throws RuntimeException, MessagingException, UnsupportedEncodingException;
    public void deleteEvent(String id);
}
