package ch.chalender.api.service.impl;

import ch.chalender.api.dal.EventsDal;
import ch.chalender.api.dto.ModerationComment;
import ch.chalender.api.model.*;
import ch.chalender.api.repository.EventsRepository;
import ch.chalender.api.service.EmailService;
import ch.chalender.api.service.EventsService;
import ch.chalender.api.service.SubscriptionSendingService;
import ch.chalender.api.service.UserService;
import ch.chalender.api.util.IcsUtil;
import jakarta.mail.MessagingException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.ProdId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
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

    @Autowired
    private EventLookupServiceImpl eventLookupService;

    @Autowired
    private EventsDal eventsDal;

    @Autowired
    private SubscriptionSendingService subscriptionSendingService;

    @Override
    public Event createEvent(Event event) {
        event = eventsRepository.save(event);
        eventLookupService.updateEventLookup(event);
        if (event.getPublicationStatus() == PublicationStatus.IN_REVIEW) {
            try {
                emailService.sendEventModeratorEmail(event);
            } catch (MessagingException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return event;
    }

    @Override
    public Event updateEvent(Event event) {
        event = eventsRepository.save(event);
        eventLookupService.updateEventLookup(event);
        if (event.getPublicationStatus() == PublicationStatus.IN_REVIEW || event.getPublicationStatus() == PublicationStatus.NEW_MODIFICATION) {
            try {
                emailService.sendEventModeratorEmail(event);
            } catch (MessagingException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
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
        if (event.getPublicationStatus() == PublicationStatus.NEW_MODIFICATION) {
            version = event.getWaitingForReview();
        } else if (event.getPublicationStatus() == PublicationStatus.PUBLISHED) {
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

        return generateIcs(version, occurrence);
    }

    @Override
    public Page<Event> listAllEvents(ModerationEventsFilter filter, Pageable pageable) {
        return eventsDal.getAllEvents(filter, pageable);
    }
    @Override
    public Page<Event> listAllEventsByUser(ModerationEventsFilter filter, User user, Pageable pageable) {
        return eventsDal.getAllEventsByUser(filter, pageable, user.getEmail());
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
            this.subscriptionSendingService.notifyUsersAboutNewEvent(event);
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
            eventLookupService.removeEventLookup(event);
        }
    }

    private Resource generateIcs(EventVersion version, EventOccurrence occurrence) {
        VEvent event = IcsUtil.generateVEventFromOccurrence(version, occurrence);

        Calendar icsCalendar = new Calendar();
        icsCalendar.add(new ProdId("-//chalender.ch//iCal4j 1.0//EN"));

        icsCalendar.add(event);

        byte[] calendarByte = icsCalendar.toString().getBytes();
        return new ByteArrayResource(calendarByte);
    }
}
