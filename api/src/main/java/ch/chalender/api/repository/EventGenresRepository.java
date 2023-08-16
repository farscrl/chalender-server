package ch.chalender.api.repository;

import ch.chalender.api.model.EventGenre;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventGenresRepository extends MongoRepository<EventGenre, Integer> {
    List<EventGenre> findByIsHiddenIsFalse(boolean hidden);
}
