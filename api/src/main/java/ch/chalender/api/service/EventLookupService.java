package ch.chalender.api.service;

import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventFilter;
import ch.chalender.api.model.EventLookup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EventLookupService {
    void recreateAllEventLookupData();
    Page<EventLookup> getAllEvents(EventFilter filter, Pageable pageable);
    List<EventLookup> getEventsInDateRange(EventFilter filter, LocalDate start, LocalDate end);
    void updateEventLookup(Event event);
    void removeEventLookup(Event event);
}
