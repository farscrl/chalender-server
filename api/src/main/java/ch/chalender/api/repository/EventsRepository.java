package ch.chalender.api.repository;

import ch.chalender.api.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventsRepository extends MongoRepository<Event, String> {
    Page<Event> findByOwnerEmail(String ownerEmail, Pageable pageable);
}
