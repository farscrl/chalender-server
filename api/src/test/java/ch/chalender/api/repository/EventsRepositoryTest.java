package ch.chalender.api.repository;

import ch.chalender.api.base.MongoDbBaseTest;
import ch.chalender.api.config.TestDataPopulatorConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EventsRepositoryTest extends MongoDbBaseTest {

    @Autowired
    private EventsRepository eventsRepository;


    @Test
    void findAllEventsInRepo() {
        assertEquals(2, eventsRepository.findAll().size());
    }
}
