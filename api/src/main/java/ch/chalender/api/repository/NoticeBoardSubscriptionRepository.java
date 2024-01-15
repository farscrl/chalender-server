package ch.chalender.api.repository;

import ch.chalender.api.model.NoticeBoardSubscription;
import ch.chalender.api.model.SubscriptionType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NoticeBoardSubscriptionRepository extends MongoRepository<NoticeBoardSubscription, String> {
    List<NoticeBoardSubscription> findAllByUsername(String username);

    List<NoticeBoardSubscription> findAllByActiveAndType(boolean active, SubscriptionType type);
}
