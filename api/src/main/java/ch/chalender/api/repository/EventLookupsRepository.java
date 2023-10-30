package ch.chalender.api.repository;

import ch.chalender.api.model.EventLookup;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventLookupsRepository extends MongoRepository<EventLookup, String> {
    void deleteAllByEventId(String eventId);
}
