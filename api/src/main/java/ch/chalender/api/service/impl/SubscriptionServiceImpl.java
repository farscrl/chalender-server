package ch.chalender.api.service.impl;

import ch.chalender.api.model.Subscription;
import ch.chalender.api.repository.SubscriptionRepository;
import ch.chalender.api.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public List<Subscription> findAllByUsername(String username) {
        return subscriptionRepository.findAllByUsername(username);
    }

    @Override
    public Subscription findById(String id) {
        return subscriptionRepository.findById(id).get();
    }

    @Override
    public Subscription add(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription update(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void delete(String id) {
        subscriptionRepository.deleteById(id);
    }
}
