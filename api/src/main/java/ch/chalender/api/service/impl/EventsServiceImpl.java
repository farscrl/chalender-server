package ch.chalender.api.service.impl;

import ch.chalender.api.dal.EventsDal;
import ch.chalender.api.dto.ModerationComment;
import ch.chalender.api.model.*;
import ch.chalender.api.repository.EventsRepository;
import ch.chalender.api.service.EmailService;
import ch.chalender.api.service.EventsService;
import ch.chalender.api.service.UserService;
import jakarta.mail.MessagingException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventLookupServiceImpl eventLookupService;

    @Autowired
    private EventsDal eventsDal;

    @Override
    public Event createEvent(Event event) {
        event = eventsRepository.save(event);
        eventLookupService.updateEventLookup(event);
        return event;
    }

    @Override
    public Event updateEvent(Event event) {
        event = eventsRepository.save(event);
        eventLookupService.updateEventLookup(event);
        return event;
    }

    @Override
    public Event getEvent(String id) {
        return eventsRepository.findById(id).orElse(null);
    }

    @Override
    public Resource getEventIcs(String id, String uid) {
        Event event = eventsRepository.findById(id).orElse(null);
        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        EventVersion version = null;
        if (event.getEventStatus() == EventStatus.NEW_MODIFICATION) {
            version = event.getWaitingForReview();
        } else if (event.getEventStatus() == EventStatus.PUBLISHED) {
            version = event.getCurrentlyPublished();
        } else {
            version = event.getDraft();
        }

        if (version == null) {
            throw new RuntimeException("Event version not found");
        }

        EventOccurrence occurrence = null;
        for (EventOccurrence eventOccurrence : version.getOccurrences()) {
            if (eventOccurrence.getOccurrenceUid().equals(uid)) {
                occurrence = eventOccurrence;
            }
        }

        if (occurrence == null) {
            throw new RuntimeException("Event occurrence not found");
        }

        return generateIcs(event, version, occurrence);
    }

    @Override
    public Page<Event> listAllEvents(ModerationEventsFilter filter, Pageable pageable) {
        return eventsDal.getAllEvents(filter, pageable);
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
        event.updateCalculatedEventFields();
        event = eventsRepository.save(event);
        eventLookupService.updateEventLookup(event);

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
            event.updateCalculatedEventFields();

            event = eventsRepository.save(event);
            eventLookupService.updateEventLookup(event);

            User user = userService.findUserByEmail(event.getOwnerEmail());
            emailService.sendEventUpdateRefusedEmail(event.getOwnerEmail(), user != null ? user.getFullName() : null, event, moderationComment.getComment());

            return event;
        }
        event.setRejected(event.getWaitingForReview());
        event.setWaitingForReview(null);
        event.updateCalculatedEventFields();

        event = eventsRepository.save(event);
        eventLookupService.updateEventLookup(event);

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
        event.updateCalculatedEventFields();

        event = eventsRepository.save(event);
        eventLookupService.updateEventLookup(event);

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
        Event event = eventsRepository.findById(id).orElse(null);
        eventsRepository.deleteById(id);
        if (event != null) {
            eventLookupService.updateEventLookup(event);
        }
    }

    private Resource generateIcs(Event chalenderEvent, EventVersion version, EventOccurrence occurrence) {
        String eventSummary = version.getTitle();
        if (occurrence.isCancelled()) {
            eventSummary = "[ANNULLÀ] " + eventSummary;
        }

        VEvent event = null;
        if (occurrence.isAllDay()) {
            event = new VEvent(occurrence.getDate(), eventSummary);
        } else if (occurrence.getEnd() == null) {
            LocalDateTime start = LocalDateTime.of(occurrence.getDate(), occurrence.getStart());
            LocalDateTime end = start.plusHours(2);
            event = new VEvent(start, end, eventSummary);
        } else {
            LocalDateTime start = LocalDateTime.of(occurrence.getDate(), occurrence.getStart());
            LocalDateTime end = LocalDateTime.of(occurrence.getDate(), occurrence.getEnd());
            event = new VEvent(start, end, eventSummary);

        }
        event.add(new Uid(occurrence.getOccurrenceUid()));

        Calendar icsCalendar = new Calendar();
        icsCalendar.add(new ProdId("-//chalender.ch//iCal4j 1.0//EN"));

        Location location = new Location("Conference Room");
        event.add(location);

        icsCalendar.add(event);

        byte[] calendarByte = icsCalendar.toString().getBytes();
        return new ByteArrayResource(calendarByte);
    }
}
