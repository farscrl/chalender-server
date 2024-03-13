package ch.chalender.api.batch.events.scheduled;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class WeeklyEventSubscriptionBatchConfiguration extends DefaultBatchConfiguration {

    @Autowired
    private WeeklyEventSubscriptionsLoader weeklyEventSubscriptionsLoader;

    @Bean
    public JobLauncher weeklyEventJobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        try {
            jobLauncher.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jobLauncher;
    }

    @Bean
    protected Step loadWeeklyEventSubscriptions(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("loadSubscriptions", jobRepository)
                .tasklet(weeklyEventSubscriptionsLoader, transactionManager)
                .build();
    }

    @Bean
    public Job weeklyEventsJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("weeklyEventSubscriptionsJob", jobRepository())
                .start(loadWeeklyEventSubscriptions(jobRepository, transactionManager))
                .build();
    }
}
