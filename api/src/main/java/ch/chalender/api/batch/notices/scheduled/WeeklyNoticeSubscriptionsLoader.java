package ch.chalender.api.batch.notices.scheduled;

import ch.chalender.api.dal.NoticeBoardItemsDal;
import ch.chalender.api.model.*;
import ch.chalender.api.repository.NoticeBoardSubscriptionRepository;
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
public class WeeklyNoticeSubscriptionsLoader implements Tasklet, StepExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(WeeklyNoticeSubscriptionsLoader.class);

    @Autowired
    private NoticeBoardSubscriptionRepository noticeBoardSubscriptionRepository;

    @Autowired
    private NoticeBoardItemsDal noticeBoardItemsDal;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    private List<NoticeBoardSubscription> subscriptions;

    private LocalDate startDate;

    private LocalDate endDate;

    private int index = 0;
    @Override
    public void beforeStep(StepExecution stepExecution) {
        StepExecutionListener.super.beforeStep(stepExecution);
        logger.debug("WeeklyNoticeSubscriptionsLoader initialized.");

        this.startDate = LocalDate.now().minusDays(8);
        this.endDate = startDate.plusDays(7);

        this.subscriptions = noticeBoardSubscriptionRepository.findAllByActiveAndType(true, SubscriptionType.WEEKLY);
        logger.debug("WeeklyNoticeSubscriptionsLoader loaded " + subscriptions.size() + " subscriptions.");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        if (index >= subscriptions.size()) {
            return null;
        }

        NoticeBoardSubscription subscription = subscriptions.get(index);

        if (!subscription.isActive()) {
            logger.error("Subscription " + subscription.getId() + " is not active.");
            return next();
        }

        if (subscription.getType() != SubscriptionType.WEEKLY) {
            logger.error("Subscription " + subscription.getId() + " is not a weekly subscription.");
            return next();
        }

        NoticeBoardFilter filter = new NoticeBoardFilter();
        filter.setSearchTerm(subscription.getSearchTerm());
        List<NoticeBoardItem> items = noticeBoardItemsDal.getNoticeBoardItemsInDateRange(filter, startDate, endDate);

        if (items.isEmpty()) {
            logger.debug("No notice board items found for subscription " + subscription.getId());
            return next();
        }
        logger.error("Found " + items.size() + " notice board items for subscription " + subscription.getId());

        User user = userService.findUserByEmail(subscription.getUsername());

        emailService.sendNoticesBoardSubscriptionWeekly(user.getEmail(), user.getFirstName(), subscription.getName(), items, subscription.getId());

        return next();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        this.index = 0;
        logger.debug("WeeklyNoticeSubscriptionsLoader finished.");

        return StepExecutionListener.super.afterStep(stepExecution);
    }

    private RepeatStatus next() {
        this.index++;
        return RepeatStatus.CONTINUABLE;
    }
}
