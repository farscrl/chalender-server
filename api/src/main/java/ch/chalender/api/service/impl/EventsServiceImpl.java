package ch.chalender.api.service.impl;

import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventFilter;
import ch.chalender.api.model.User;
import ch.chalender.api.repository.EventsRepository;
import ch.chalender.api.service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    private EventsRepository eventsRepository;

    @Override
    public Event createEvent(Event event) {
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
    public Event acceptChanges(String id) {
        Event event = eventsRepository.findById(id).orElse(null);

        if (event == null) {
            return null;
        }

        event.setCurrentlyPublished(event.getWaitingForReview());
        return eventsRepository.save(event);
    }

    @Override
    public Event refuseChanges(String id) {
        Event event = eventsRepository.findById(id).orElse(null);

        if (event == null) {
            return null;
        }

        event.setWaitingForReview(null);
        return eventsRepository.save(event);
    }
}
