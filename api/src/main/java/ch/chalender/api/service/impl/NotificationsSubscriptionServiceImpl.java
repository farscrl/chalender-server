package ch.chalender.api.service.impl;

import ch.chalender.api.model.NotificationsSubscription;
import ch.chalender.api.repository.NotificationsSubscriptionRepository;
import ch.chalender.api.service.NotificationsSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationsSubscriptionServiceImpl implements NotificationsSubscriptionService {

    @Autowired
    private NotificationsSubscriptionRepository notificationsSubscriptionRepository;

    @Override
    public List<NotificationsSubscription> findAllByUsername(String username) {
        return notificationsSubscriptionRepository.findAllByUsername(username);
    }

    @Override
    public NotificationsSubscription findById(String id) {
        return notificationsSubscriptionRepository.findById(id).get();
    }

    @Override
    public NotificationsSubscription add(NotificationsSubscription subscription) {
        return notificationsSubscriptionRepository.save(subscription);
    }

    @Override
    public NotificationsSubscription update(NotificationsSubscription subscription) {
        return notificationsSubscriptionRepository.save(subscription);
    }

    @Override
    public void delete(String id) {
        notificationsSubscriptionRepository.deleteById(id);
    }
}
