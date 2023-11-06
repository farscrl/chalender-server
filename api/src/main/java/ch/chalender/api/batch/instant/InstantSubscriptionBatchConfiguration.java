package ch.chalender.api.batch.instant;

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
public class InstantSubscriptionBatchConfiguration extends DefaultBatchConfiguration {

    @Autowired
    private InstantSubscriptionsLoader instantSubscriptionsLoader;

    @Bean
    public JobLauncher instantJobLauncher(JobRepository jobRepository) throws Exception {
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
    protected Step loadInstantSubscriptions(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("loadSubscriptions", jobRepository)
                .tasklet(instantSubscriptionsLoader, transactionManager)
                .build();
    }

    @Bean
    public Job instantJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("instantSubscriptionsJob", jobRepository)
                .start(loadInstantSubscriptions(jobRepository, transactionManager))
                .build();
    }
}
