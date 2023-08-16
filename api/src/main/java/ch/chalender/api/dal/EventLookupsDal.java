package ch.chalender.api.dal;

import ch.chalender.api.model.EventFilter;
import ch.chalender.api.model.EventLookup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventLookupsDal {
    public Page<EventLookup> getAllEvents(EventFilter filter, Pageable pageable);
}
