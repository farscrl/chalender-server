package ch.chalender.api.batch.notices.instant;

import ch.chalender.api.model.*;
import ch.chalender.api.repository.NoticeBoardItemsRepository;
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

import java.util.List;

@Component
public class InstantNoticesSubscriptionsLoader implements Tasklet, StepExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(InstantNoticesSubscriptionsLoader.class);

    @Autowired
    private NoticeBoardSubscriptionRepository noticeBoardSubscriptionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NoticeBoardItemsRepository noticeBoardItemsRepository;

    private List<NoticeBoardSubscription> subscriptions;
    private NoticeBoardItem item;


    private int index = 0;
    @Override
    public void beforeStep(StepExecution stepExecution) {
        StepExecutionListener.super.beforeStep(stepExecution);
        logger.debug("InstantNoticesSubscriptionsLoader initialized.");

        String itemId = (String) stepExecution.getJobExecution().getJobParameters().getParameters().get("jobId").getValue();
        if (itemId == null) {
            logger.error("No notices board item id found in job parameters.");
            return;
        }
        item = noticeBoardItemsRepository.findById(itemId).orElseThrow();

        this.subscriptions = noticeBoardSubscriptionRepository.findAllByActiveAndType(true, SubscriptionType.INSTANT);
        logger.debug("InstantNoticesSubscriptionsLoader loaded " + subscriptions.size() + " subscriptions.");
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

        if (subscription.getType() != SubscriptionType.INSTANT) {
            logger.error("Subscription " + subscription.getId() + " is not an instant subscription.");
            return next();
        }

        boolean sendEmail = true;
        NoticeBoardItemVersion version = item.getCurrentlyPublished();
        if (
                !subscription.getSearchTerm().isEmpty() &&
                !(
                        version.getTitle().contains(subscription.getSearchTerm()) ||
                        version.getDescription().contains(subscription.getSearchTerm()) ||
                        version.getContactData().contains(subscription.getSearchTerm())
                )
        ) {
            sendEmail = false;
        }

        if (!sendEmail) {
            return next();
        }

        User user = userService.findUserByEmail(subscription.getUsername());

        emailService.sendNoticesBoardSubscriptionInstant(user.getEmail(), user.getFirstName(), subscription.getName(), item, subscription.getId());

        return next();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        this.index = 0;
        logger.debug("InstantNoticesSubscriptionsLoader finished.");

        return StepExecutionListener.super.afterStep(stepExecution);
    }

    private RepeatStatus next() {
        this.index++;
        return RepeatStatus.CONTINUABLE;
    }
}
