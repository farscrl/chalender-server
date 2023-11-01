package ch.chalender.api.controller.user;

import ch.chalender.api.config.CurrentUser;
import ch.chalender.api.dto.LocalUser;
import ch.chalender.api.model.Subscription;
import ch.chalender.api.model.User;
import ch.chalender.api.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/user/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("")
    public ResponseEntity<List<Subscription>> listAllSubscriptions(@CurrentUser LocalUser localUser) {
        User user = localUser.getUser();
        return ResponseEntity.ok(subscriptionService.findAllByUsername(user.getEmail()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscription(@PathVariable String id, @CurrentUser LocalUser localUser) {
        Subscription subscription = subscriptionService.findById(id);

        if (!subscription.getUsername().equals(localUser.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(subscription);
    }

    @PostMapping("")
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscriptionToCreate, @CurrentUser LocalUser localUser) {
        subscriptionToCreate.setUsername(localUser.getUser().getEmail());
        return ResponseEntity.ok(subscriptionService.add(subscriptionToCreate));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Subscription> updateSubscription(@PathVariable String id, @RequestBody Subscription subscriptionToUpdate, @CurrentUser LocalUser localUser) {
        Subscription subscription = subscriptionService.findById(id);

        if (!subscription.getUsername().equals(localUser.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        subscriptionToUpdate.setId(id);
        return ResponseEntity.ok(subscriptionService.update(subscriptionToUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable String id, @CurrentUser LocalUser localUser) {
        Subscription subscription = subscriptionService.findById(id);

        if (!subscription.getUsername().equals(localUser.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        subscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
