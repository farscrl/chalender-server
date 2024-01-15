package ch.chalender.api.batch.events.instant;

import ch.chalender.api.model.*;
import ch.chalender.api.repository.EventsRepository;
import ch.chalender.api.repository.EventsSubscriptionRepository;
import ch.chalender.api.service.EmailService;
import ch.chalender.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InstantEventSubscriptionsLoader implements Tasklet, StepExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(InstantEventSubscriptionsLoader.class);

    @Autowired
    private EventsSubscriptionRepository eventsSubscriptionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EventsRepository eventsRepository;

    private List<EventsSubscription> subscriptions;
    private Event event;


    private int index = 0;
    @Override
    public void beforeStep(StepExecution stepExecution) {
        StepExecutionListener.super.beforeStep(stepExecution);
        logger.debug("InstantEventSubscriptionsLoader initialized.");

        String eventId = (String) stepExecution.getJobExecution().getJobParameters().getParameters().get("jobId").getValue();
        if (eventId == null) {
            logger.error("No event id found in job parameters.");
            return;
        }
        event = eventsRepository.findById(eventId).orElseThrow();

        this.subscriptions = eventsSubscriptionRepository.findAllByActiveAndType(true, SubscriptionType.INSTANT);
        logger.debug("InstantEventSubscriptionsLoader loaded " + subscriptions.size() + " subscriptions.");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        if (index >= subscriptions.size()) {
            return null;
        }

        EventsSubscription subscription = subscriptions.get(index);

        if (!subscription.isActive()) {
            logger.error("Subscription " + subscription.getId() + " is not active.");
            return next();
        }

        if (subscription.getType() != SubscriptionType.INSTANT) {
            logger.error("Subscription " + subscription.getId() + " is not an instant subscription.");
            return next();
        }

        boolean sendEmail = true;
        EventVersion version = event.getCurrentlyPublished();
        if (
                !subscription.getSearchTerm().isEmpty() &&
                !(
                        version.getTitle().contains(subscription.getSearchTerm()) ||
                        version.getDescription().contains(subscription.getSearchTerm()) ||
                        version.getLocation().contains(subscription.getSearchTerm()) ||
                        version.getAddress().contains(subscription.getSearchTerm()) ||
                        version.getOrganiser().contains(subscription.getSearchTerm())
                )
        ) {
            sendEmail = false;
        } else if (!subscription.getGenres().isEmpty() && subscription.getGenres().stream().noneMatch(version.getGenres()::contains)) {
            sendEmail = false;
        } else if (!subscription.getRegions().isEmpty() && subscription.getRegions().stream().noneMatch(version.getRegions()::contains)) {
            sendEmail = false;
        } else if (!subscription.getEventLanguages().isEmpty() && subscription.getEventLanguages().stream().noneMatch(version.getEventLanguages()::contains)) {
            sendEmail = false;
        }

        if (!sendEmail) {
            return next();
        }

        User user = userService.findUserByEmail(subscription.getUsername());

        emailService.sendEventSubscriptionInstant(user.getEmail(), user.getFirstName(), subscription.getName(), event, subscription.getId());

        return next();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        this.index = 0;
        logger.debug("InstantEventSubscriptionsLoader finished.");

        return StepExecutionListener.super.afterStep(stepExecution);
    }

    private RepeatStatus next() {
        this.index++;
        return RepeatStatus.CONTINUABLE;
    }
}
