package ch.chalender.api.service.impl;

import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventFilter;
import ch.chalender.api.repository.EventsRepository;
import ch.chalender.api.service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    private EventsRepository eventsRepository;

    @Override
    public List<Event> getPublicEvents(EventFilter filter) {
        return eventsRepository.findAll();
    }

    @Override
    public Event createEvent(Event event) {
        return eventsRepository.save(event);
    }

    @Override
    public Event getEvent(String id) {
        return eventsRepository.findById(id).orElse(null);
    }
}
