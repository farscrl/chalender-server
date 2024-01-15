package ch.chalender.api.controller.user;

import ch.chalender.api.config.CurrentUser;
import ch.chalender.api.dto.LocalUser;
import ch.chalender.api.model.NoticeBoardSubscription;
import ch.chalender.api.model.User;
import ch.chalender.api.service.NotificationsSubscriptionService;
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
@RequestMapping("/api/user/subscriptions/notices")
public class NoticesSubscriptionController {

    @Autowired
    private NotificationsSubscriptionService subscriptionService;

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NoticeBoardSubscription>> listAllSubscriptions(@CurrentUser LocalUser localUser) {
        User user = localUser.getUser();
        return ResponseEntity.ok(subscriptionService.findAllByUsername(user.getEmail()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<NoticeBoardSubscription> getSubscription(@PathVariable String id, @CurrentUser LocalUser localUser) {
        NoticeBoardSubscription subscription = subscriptionService.findById(id);

        if (!subscription.getUsername().equals(localUser.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(subscription);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/{id}/disable")
    public ResponseEntity<NoticeBoardSubscription> disableSubscription(@PathVariable String id) {
        NoticeBoardSubscription subscription = subscriptionService.findById(id);
        subscription.setActive(false);
        return ResponseEntity.ok(subscriptionService.update(subscription));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<NoticeBoardSubscription> createSubscription(@RequestBody NoticeBoardSubscription subscriptionToCreate, @CurrentUser LocalUser localUser) {
        subscriptionToCreate.setUsername(localUser.getUser().getEmail());
        return ResponseEntity.ok(subscriptionService.add(subscriptionToCreate));
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<NoticeBoardSubscription> updateSubscription(@PathVariable String id, @RequestBody NoticeBoardSubscription subscriptionToUpdate, @CurrentUser LocalUser localUser) {
        NoticeBoardSubscription subscription = subscriptionService.findById(id);

        if (!subscription.getUsername().equals(localUser.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        subscriptionToUpdate.setId(id);
        subscriptionToUpdate.setUsername(subscription.getUsername());
        return ResponseEntity.ok(subscriptionService.update(subscriptionToUpdate));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteSubscription(@PathVariable String id, @CurrentUser LocalUser localUser) {
        NoticeBoardSubscription subscription = subscriptionService.findById(id);

        if (!subscription.getUsername().equals(localUser.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        subscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
