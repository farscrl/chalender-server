package ch.chalender.api.config;

import ch.chalender.api.repository.EventsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

public class TestDataPopulatorConfiguration {
    @Bean
    public Jackson2RepositoryPopulatorFactoryBean getRepositoryPopulator(EventsRepository eventsRepository) {
        eventsRepository.deleteAll();
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        factory.setResources(new Resource[]{new ClassPathResource("testdata/events.json")});

        return factory;
    }
}
