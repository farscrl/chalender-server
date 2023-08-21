package ch.chalender.api.base;

import ch.chalender.api.controller.admin.FixturesController;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
@AutoConfigureMockMvc
public class MongoDbBaseTest {

    @BeforeAll
    public static void beforeAll(@Autowired FixturesService fixturesService) throws IOException {
        //mongoDbContainer.start();
        System.out.println("Loading fixtures for test...");
        fixturesService.loadEventGenreFixtures();
        fixturesService.loadEventRegionFixtures();
        fixturesService.loadEventLanguagesFixtures();
        fixturesService.loadEventFixtures();
        System.out.println("Loaded fixtures for test.");
    }
}
