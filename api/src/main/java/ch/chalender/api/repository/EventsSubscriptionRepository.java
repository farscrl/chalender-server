package ch.chalender.api.repository;

import ch.chalender.api.model.EventsSubscription;
import ch.chalender.api.model.SubscriptionType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventsSubscriptionRepository extends MongoRepository<EventsSubscription, String> {
    List<EventsSubscription> findAllByUsername(String username);

    List<EventsSubscription> findAllByActiveAndType(boolean active, SubscriptionType type);
}
