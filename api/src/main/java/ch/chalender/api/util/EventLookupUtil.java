package ch.chalender.api.util;

import ch.chalender.api.service.EventLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class EventLookupUtil implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(EventLookupUtil.class);

    @Autowired
    private EventLookupService eventLookupService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("Recreating event lookup data...");
        long start = System.currentTimeMillis();
        eventLookupService.recreateAllEventLookupData();
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        logger.info("Recreated event lookup data (took " + timeElapsed + "ms)");
    }
}
