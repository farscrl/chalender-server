package ch.chalender.api.base;

import ch.chalender.api.service.FixturesService;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class MongoDbBaseTest {

    public static GenericContainer mongoDbContainer = new GenericContainer("mongo:5.0.2")
            .withExposedPorts(27017)
            .waitingFor(Wait.forLogMessage(".*Waiting for connections.*\\n", 1))
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "root")
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "root");

    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        mongoDbContainer.start();
        registry.add("spring.data.mongodb.host", mongoDbContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDbContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.database", () -> "chalender");
        registry.add("spring.data.mongodb.username", () -> "root");
        registry.add("spring.data.mongodb.password", () -> "root");
    }

    @BeforeAll
    public static void beforeAll(@Autowired FixturesService fixturesService) throws IOException {
        System.out.println("Loading fixtures for test...");
        fixturesService.loadEventGenreFixtures();
        fixturesService.loadEventRegionFixtures();
        fixturesService.loadEventLanguagesFixtures();
        fixturesService.loadEventFixtures();
        System.out.println("Loaded fixtures for test.");
    }
}
