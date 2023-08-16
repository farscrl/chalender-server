package ch.chalender.api.repository;

import ch.chalender.api.base.MongoDbBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EventsRepositoryTest extends MongoDbBaseTest {

    @Autowired
    private EventsRepository eventsRepository;


    @Test
    void findAllEventsInRepo() {
        assertEquals(16, eventsRepository.findAll().size());
    }
}
