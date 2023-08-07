package ch.chalender.api.repository;

import ch.chalender.api.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventsRepository extends MongoRepository<Event, String> {
}
