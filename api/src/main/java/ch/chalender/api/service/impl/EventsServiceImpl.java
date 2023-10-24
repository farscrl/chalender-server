package ch.chalender.api.service.impl;

import ch.chalender.api.dto.ModerationComment;
import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventFilter;
import ch.chalender.api.model.EventVersion;
import ch.chalender.api.model.User;
import ch.chalender.api.repository.EventsRepository;
import ch.chalender.api.service.EmailService;
import ch.chalender.api.service.EventsService;
import ch.chalender.api.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Override
    public Event createEvent(Event event) {
        return eventsRepository.save(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventsRepository.save(event);
    }

    @Override
    public Event getEvent(String id) {
        return eventsRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Event> listAllEvents(EventFilter filter, Pageable pageable) {
        return eventsRepository.findAll(pageable);
    }
    @Override
    public Page<Event> listAllEventsByUser(User user, Pageable pageable) {
        return eventsRepository.findByOwnerEmail(user.getEmail(), pageable);
    }

    @Override
    public Event acceptChanges(String id, ModerationComment moderationComment) throws RuntimeException, MessagingException, UnsupportedEncodingException {
        Event event = eventsRepository.findById(id).orElse(null);
        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        boolean isNew = event.getCurrentlyPublished() == null;

        event.setCurrentlyPublished(event.getWaitingForReview());
        event.setWaitingForReview(null);
        event.setRejected(null);
        event.setDraft(null);
        event = eventsRepository.save(event);

        User user = userService.findUserByEmail(event.getOwnerEmail());
        if (isNew) {
            emailService.sendEventPublishedEmail(event.getOwnerEmail(), user != null ? user.getFullName() : null, event, moderationComment.getComment());
            return event;
        }
        emailService.sendEventUpdateAcceptedEmail(event.getOwnerEmail(), user != null ? user.getFullName() : null, event, moderationComment.getComment());
        return event;
    }

    @Override
    public Event refuseChanges(String id, ModerationComment moderationComment) throws RuntimeException, MessagingException, UnsupportedEncodingException {
        Event event = eventsRepository.findById(id).orElse(null);
        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        if (event.getCurrentlyPublished() != null) {
            event.setWaitingForReview(null);

            event = eventsRepository.save(event);

            User user = userService.findUserByEmail(event.getOwnerEmail());
            emailService.sendEventUpdateRefusedEmail(event.getOwnerEmail(), user != null ? user.getFullName() : null, event, moderationComment.getComment());

            return event;
        }
        event.setRejected(event.getWaitingForReview());
        event.setWaitingForReview(null);

        event = eventsRepository.save(event);

        User user = userService.findUserByEmail(event.getOwnerEmail());
        emailService.sendEventRefusedEmail(event.getOwnerEmail(), user != null ? user.getFullName() : null, event, moderationComment.getComment());

        return event;
    }

    @Override
    public Event changeAndPublish(String id, EventVersion eventVersion) throws RuntimeException, MessagingException, UnsupportedEncodingException {
        Event event = eventsRepository.findById(id).orElse(null);
        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        boolean isNew = event.getCurrentlyPublished() == null;
        boolean isUpdate = event.getCurrentlyPublished() != null && event.getWaitingForReview() != null;

        event.setCurrentlyPublished(eventVersion);
        event.setWaitingForReview(null);
        event.setRejected(null);
        event.setDraft(null);

        event = eventsRepository.save(event);

        User user = userService.findUserByEmail(event.getOwnerEmail());
        if (isNew) {
            emailService.sendEventPublishedEmail(event.getOwnerEmail(), user != null ? user.getFullName() : null, event, eventVersion.getModerationComment().getComment());
            return event;
        }
        if (isUpdate) {
            emailService.sendEventUpdateAcceptedEmail(event.getOwnerEmail(), user != null ? user.getFullName() : null, event, eventVersion.getModerationComment().getComment());
            return event;
        }

        return event;
    }

    @Override
    public void deleteEvent(String id) {
        eventsRepository.deleteById(id);
    }
}
