package ch.chalender.api.repository;

import ch.chalender.api.model.NotificationsSubscription;
import ch.chalender.api.model.SubscriptionType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationsSubscriptionRepository extends MongoRepository<NotificationsSubscription, String> {
    List<NotificationsSubscription> findAllByUsername(String username);

    List<NotificationsSubscription> findAllByActiveAndType(boolean active, SubscriptionType type);
}
