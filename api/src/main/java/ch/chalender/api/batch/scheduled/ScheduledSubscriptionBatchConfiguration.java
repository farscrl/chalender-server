package ch.chalender.api.batch.scheduled;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class ScheduledSubscriptionBatchConfiguration extends DefaultBatchConfiguration {

    @Autowired
    private SubscriptionsLoader subscriptionsLoader;

    @Bean
    public JobLauncher jobLauncher() {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        try {
            jobLauncher.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jobLauncher;
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    protected Step loadSubscriptions(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("loadSubscriptions", jobRepository)
                .tasklet(subscriptionsLoader, transactionManager)
                .build();
    }

    @Bean
    public Job job() {
        return new JobBuilder("scheduledSubscriptionsJob", jobRepository())
                .start(loadSubscriptions(jobRepository(), transactionManager()))
                .build();
    }
}
