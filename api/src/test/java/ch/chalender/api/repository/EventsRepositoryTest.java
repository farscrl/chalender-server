package ch.chalender.api.repository;

import ch.chalender.api.config.TestDataPopulatorConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@Import(TestDataPopulatorConfiguration.class)
public class EventsRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private EventsRepository eventsRepository;


    @Test
    void findByLocationWithin_FindsRestaurantsWithinAGivenDistance() {
        assertEquals(2, eventsRepository.findAll().size());
    }

    @AfterEach
    void cleanUpDatabase() {
        mongoTemplate.getDb().drop();
    }
}
