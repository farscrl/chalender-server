package ch.chalender.api.repository;

import ch.chalender.api.model.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    List<Subscription> findAllByUsername(String username);

    List<Subscription> findAllByActiveAndType(boolean active, Subscription.SubscriptionType type);
}
