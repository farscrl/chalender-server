package ch.chalender.api.service.impl;

import ch.chalender.api.converter.EventLookupConverter;
import ch.chalender.api.dal.EventLookupsDal;
import ch.chalender.api.model.EventFilter;
import ch.chalender.api.model.EventLookup;
import ch.chalender.api.repository.EventLookupsRepository;
import ch.chalender.api.repository.EventsRepository;
import ch.chalender.api.service.EventLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventLookupServiceImpl implements EventLookupService {

   @Autowired
   private EventsRepository eventsRepository;

    @Autowired
    private EventLookupsRepository eventLookupsRepository;

    @Autowired
    private EventLookupsDal eventLookupsDal;

    public void recreateAllEventLookupData() {
        eventLookupsRepository.deleteAll();

        eventsRepository.findAll().forEach(event -> {
            List<EventLookup> eventLookups = EventLookupConverter.getEventLookups(event);
            eventLookupsRepository.saveAll(eventLookups);
        });
    }

    public Page<EventLookup> getAllEvents(EventFilter filter, Pageable pageable) {
        return eventLookupsDal.getAllEvents(filter, pageable);
    }
}
