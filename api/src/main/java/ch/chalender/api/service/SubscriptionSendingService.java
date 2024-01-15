package ch.chalender.api.service;

import ch.chalender.api.batch.events.instant.InstantEventSubscriptionBatchConfiguration;
import ch.chalender.api.batch.events.scheduled.WeeklyEventSubscriptionBatchConfiguration;
import ch.chalender.api.batch.notices.instant.InstantNoticesSubscriptionBatchConfiguration;
import ch.chalender.api.batch.notices.scheduled.WeeklyNoticeSubscriptionBatchConfiguration;
import ch.chalender.api.model.Event;
import ch.chalender.api.model.NoticeBoardItem;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Service
public class SubscriptionSendingService {

    @Autowired
    private WeeklyEventSubscriptionBatchConfiguration weeklyEventSubscriptionBatchConfiguration;

    @Autowired
    private InstantEventSubscriptionBatchConfiguration instantEventSubscriptionBatchConfiguration;

    @Autowired
    private WeeklyNoticeSubscriptionBatchConfiguration weeklyNoticeSubscriptionBatchConfiguration;

    @Autowired
    private InstantNoticesSubscriptionBatchConfiguration instantNoticesSubscriptionBatchConfiguration;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Scheduled(cron = "${chalender.cronWeeklyEmailsEvents}")
    public void sendEventSubscriptions() {
        JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
                .toJobParameters();
        try {
            var job = weeklyEventSubscriptionBatchConfiguration.weeklyEventJobLauncher(jobRepository).run(weeklyEventSubscriptionBatchConfiguration.weeklyJob(jobRepository, transactionManager), params);
        } catch (JobExecutionAlreadyRunningException | JobParametersInvalidException |
                 JobInstanceAlreadyCompleteException | JobRestartException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void notifyUsersAboutNewEvent(Event event) {
        JobParameters params = new JobParametersBuilder().addString("jobId", event.getId()).toJobParameters();
        try {
            var job = instantEventSubscriptionBatchConfiguration.instantEventJobLauncher(jobRepository).run(instantEventSubscriptionBatchConfiguration.instantJob(jobRepository, transactionManager), params);
        } catch (JobExecutionAlreadyRunningException | JobParametersInvalidException |
                 JobInstanceAlreadyCompleteException | JobRestartException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "${chalender.cronWeeklyEmailsNoticeBoard}")
    public void sendNoticeBoardSubscriptions() {
        JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
                .toJobParameters();
        try {
            var job = weeklyNoticeSubscriptionBatchConfiguration.weeklyNoticeJobLauncher(jobRepository).run(weeklyNoticeSubscriptionBatchConfiguration.weeklyJob(jobRepository, transactionManager), params);
        } catch (JobExecutionAlreadyRunningException | JobParametersInvalidException |
                 JobInstanceAlreadyCompleteException | JobRestartException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void notifyUsersAboutNewNoticeBoardItem(NoticeBoardItem item) {
        JobParameters params = new JobParametersBuilder().addString("jobId", item.getId()).toJobParameters();
        try {
            var job = instantNoticesSubscriptionBatchConfiguration.instantNoticeJobLauncher(jobRepository).run(instantNoticesSubscriptionBatchConfiguration.instantJob(jobRepository, transactionManager), params);
        } catch (JobExecutionAlreadyRunningException | JobParametersInvalidException |
                 JobInstanceAlreadyCompleteException | JobRestartException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
