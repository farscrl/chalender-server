package ch.chalender.api.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("events")
@Data
public class Event {
    private String id;

    private EventVersion currentlyPublished;
    private EventVersion draft;
    private EventVersion lastReviewed;

    private List<EventVersion> versions;
}
