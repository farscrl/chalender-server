package ch.chalender.api.base;

import ch.chalender.api.config.TestDataPopulatorConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestDataPopulatorConfiguration.class)
public class MongoDbBaseTest {

}
