package ch.chalender.api.service;

import ch.chalender.api.model.EventsSubscription;

import java.util.List;

public interface EventsSubscriptionService {

    List<EventsSubscription> findAllByUsername(String username);

    EventsSubscription findById(String id);

    EventsSubscription add(EventsSubscription subscription);

    EventsSubscription update(EventsSubscription subscription);

    void delete(String id);
}
