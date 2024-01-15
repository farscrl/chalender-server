package ch.chalender.api.service.impl;

import ch.chalender.api.model.NoticeBoardSubscription;
import ch.chalender.api.repository.NoticeBoardSubscriptionRepository;
import ch.chalender.api.service.NotificationsSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationsSubscriptionServiceImpl implements NotificationsSubscriptionService {

    @Autowired
    private NoticeBoardSubscriptionRepository noticeBoardSubscriptionRepository;

    @Override
    public List<NoticeBoardSubscription> findAllByUsername(String username) {
        return noticeBoardSubscriptionRepository.findAllByUsername(username);
    }

    @Override
    public NoticeBoardSubscription findById(String id) {
        return noticeBoardSubscriptionRepository.findById(id).get();
    }

    @Override
    public NoticeBoardSubscription add(NoticeBoardSubscription subscription) {
        subscription.setId(null);
        return noticeBoardSubscriptionRepository.save(subscription);
    }

    @Override
    public NoticeBoardSubscription update(NoticeBoardSubscription subscription) {
        return noticeBoardSubscriptionRepository.save(subscription);
    }

    @Override
    public void delete(String id) {
        noticeBoardSubscriptionRepository.deleteById(id);
    }
}
