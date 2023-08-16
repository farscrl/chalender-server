package ch.chalender.api.repository;

import ch.chalender.api.model.EventRegion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRegionsRepository extends MongoRepository<EventRegion, Integer> {
    List<EventRegion> findByIsHiddenIsFalse(boolean hidden);
}
