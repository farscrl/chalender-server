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
    public static void beforeAll(@Autowired FixturesController fixturesController) throws IOException {
        System.out.println("Loading fixtures for test...");
        fixturesController.loadEventFixtures();
        System.out.println("Loaded fixtures for test.");
    }
}
