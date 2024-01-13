package ch.chalender.api.service;

import ch.chalender.api.model.NotificationsSubscription;

import java.util.List;

public interface NotificationsSubscriptionService {

    List<NotificationsSubscription> findAllByUsername(String username);

    NotificationsSubscription findById(String id);

    NotificationsSubscription add(NotificationsSubscription subscription);

    NotificationsSubscription update(NotificationsSubscription subscription);

    void delete(String id);
}
