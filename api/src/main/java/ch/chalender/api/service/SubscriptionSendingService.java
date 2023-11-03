package ch.chalender.api.service;

import ch.chalender.api.batch.instant.InstantSubscriptionBatchConfiguration;
import ch.chalender.api.batch.scheduled.WeeklySubscriptionBatchConfiguration;
import ch.chalender.api.model.Event;
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
    private WeeklySubscriptionBatchConfiguration weeklySubscriptionBatchConfiguration;

    @Autowired
    private InstantSubscriptionBatchConfiguration instantSubscriptionBatchConfiguration;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendSubscriptions() {
        JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
                .toJobParameters();
        try {
            var job = weeklySubscriptionBatchConfiguration.weeklyJobLauncher(jobRepository).run(weeklySubscriptionBatchConfiguration.weeklyJob(jobRepository, transactionManager), params);

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
            var job = instantSubscriptionBatchConfiguration.instantJobLauncher(jobRepository).run(instantSubscriptionBatchConfiguration.instantJob(jobRepository, transactionManager), params);

        } catch (JobExecutionAlreadyRunningException | JobParametersInvalidException |
                 JobInstanceAlreadyCompleteException | JobRestartException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
