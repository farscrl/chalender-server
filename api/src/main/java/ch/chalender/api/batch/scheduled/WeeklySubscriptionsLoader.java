package ch.chalender.api.batch.scheduled;

import ch.chalender.api.dal.EventLookupsDal;
import ch.chalender.api.model.*;
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

import java.time.LocalDate;
import java.util.List;

@Component
public class WeeklySubscriptionsLoader implements Tasklet, StepExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(WeeklySubscriptionsLoader.class);

    @Autowired
    private EventsSubscriptionRepository eventsSubscriptionRepository;

    @Autowired
    private EventLookupsDal eventLookupsDal;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    private List<EventsSubscription> subscriptions;

    private LocalDate startDate;

    private LocalDate endDate;

    private int index = 0;
    @Override
    public void beforeStep(StepExecution stepExecution) {
        StepExecutionListener.super.beforeStep(stepExecution);
        logger.debug("WeeklySubscriptionsLoader initialized.");

        this.startDate = LocalDate.now();
        this.endDate = startDate.plusDays(50); // TODO: change to 15

        this.subscriptions = eventsSubscriptionRepository.findAllByActiveAndType(true, SubscriptionType.WEEKLY);
        logger.debug("WeeklySubscriptionsLoader loaded " + subscriptions.size() + " subscriptions.");
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

        if (subscription.getType() != SubscriptionType.WEEKLY) {
            logger.error("Subscription " + subscription.getId() + " is not a weekly subscription.");
            return next();
        }

        EventFilter filter = new EventFilter();
        filter.setGenres(subscription.getGenres().stream().map(EventGenre::getId).toList());
        filter.setRegions(subscription.getRegions().stream().map(EventRegion::getId).toList());
        filter.setSearchTerm(subscription.getSearchTerm());
        List<EventLookup> events = eventLookupsDal.getEventsInDateRange(filter, startDate, endDate);

        if (events.isEmpty()) {
            logger.debug("No events found for subscription " + subscription.getId());
            return next();
        }
        logger.error("Found " + events.size() + " events for subscription " + subscription.getId());

        User user = userService.findUserByEmail(subscription.getUsername());

        emailService.sendEventSubscriptionWeekly(user.getEmail(), user.getFirstName(), subscription.getName(), events, subscription.getId());

        index++;

        return RepeatStatus.CONTINUABLE;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        this.index = 0;
        logger.debug("WeeklySubscriptionsLoader finished.");

        return StepExecutionListener.super.afterStep(stepExecution);
    }

    private RepeatStatus next() {
        this.index++;
        return RepeatStatus.CONTINUABLE;
    }
}
