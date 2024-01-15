package ch.chalender.api.service;

import ch.chalender.api.model.NoticeBoardSubscription;

import java.util.List;

public interface NotificationsSubscriptionService {

    List<NoticeBoardSubscription> findAllByUsername(String username);

    NoticeBoardSubscription findById(String id);

    NoticeBoardSubscription add(NoticeBoardSubscription subscription);

    NoticeBoardSubscription update(NoticeBoardSubscription subscription);

    void delete(String id);
}
