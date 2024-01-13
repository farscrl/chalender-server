package ch.chalender.api.service.impl;

import ch.chalender.api.model.EventsSubscription;
import ch.chalender.api.repository.EventsSubscriptionRepository;
import ch.chalender.api.service.EventsSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventsSubscriptionServiceImpl implements EventsSubscriptionService {

    @Autowired
    private EventsSubscriptionRepository eventsSubscriptionRepository;

    @Override
    public List<EventsSubscription> findAllByUsername(String username) {
        return eventsSubscriptionRepository.findAllByUsername(username);
    }

    @Override
    public EventsSubscription findById(String id) {
        return eventsSubscriptionRepository.findById(id).get();
    }

    @Override
    public EventsSubscription add(EventsSubscription subscription) {
        subscription.setId(null);
        return eventsSubscriptionRepository.save(subscription);
    }

    @Override
    public EventsSubscription update(EventsSubscription subscription) {
        return eventsSubscriptionRepository.save(subscription);
    }

    @Override
    public void delete(String id) {
        eventsSubscriptionRepository.deleteById(id);
    }
}
