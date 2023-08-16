package ch.chalender.api.config;

import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "chalender";
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}
