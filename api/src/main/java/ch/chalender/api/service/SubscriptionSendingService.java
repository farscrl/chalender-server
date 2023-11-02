package ch.chalender.api.service;

import ch.chalender.api.batch.scheduled.ScheduledSubscriptionBatchConfiguration;
import ch.chalender.api.model.Event;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionSendingService {

    @Autowired
    private ScheduledSubscriptionBatchConfiguration scheduledSubscriptionBatchConfiguration;

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendSubscriptions() {
        JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
                .toJobParameters();
        try {
            var job = scheduledSubscriptionBatchConfiguration.jobLauncher().run(scheduledSubscriptionBatchConfiguration.job(), params);

        } catch (JobExecutionAlreadyRunningException | JobParametersInvalidException |
                 JobInstanceAlreadyCompleteException | JobRestartException e) {
            e.printStackTrace();
        }
    }

    public void notifyUsers(Event event) {
        // TODO
    }
}
