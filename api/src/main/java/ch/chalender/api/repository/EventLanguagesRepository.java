package ch.chalender.api.repository;

import ch.chalender.api.model.EventLanguage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventLanguagesRepository extends MongoRepository<EventLanguage, String> {
    List<EventLanguage> findByIsHiddenIsFalse(boolean hidden);
}
