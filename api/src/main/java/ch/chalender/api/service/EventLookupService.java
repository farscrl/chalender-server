package ch.chalender.api.service;

import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventFilter;
import ch.chalender.api.model.EventLookup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventLookupService {
    void recreateAllEventLookupData();
    Page<EventLookup> getAllEvents(EventFilter filter, Pageable pageable);
    void updateEventLookup(Event event);
    void removeEventLookup(Event event);
}
