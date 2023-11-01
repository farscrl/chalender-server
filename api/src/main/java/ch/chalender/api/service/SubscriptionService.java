package ch.chalender.api.service;

import ch.chalender.api.model.Subscription;

import java.util.List;

public interface SubscriptionService {

    List<Subscription> findAllByUsername(String username);

    Subscription findById(String id);

    Subscription add(Subscription subscription);

    Subscription update(Subscription subscription);

    void delete(String id);
}
