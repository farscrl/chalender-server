package ch.chalender.api.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("events")
@Data
public class Event {
    private String id;

    private EventVersion draft;
    private EventVersion currentlyPublished;
    private EventVersion waitingForReview;

    private List<EventVersion> versions;

    public EventStatus getEventStatus() {
        if (draft != null && currentlyPublished == null && waitingForReview == null) {
            return EventStatus.DRAFT;
        }
        if (waitingForReview != null && currentlyPublished == null && draft == null) {
            return EventStatus.IN_REVIEW;
        }
        if (currentlyPublished != null && waitingForReview != null && draft == null) {
            return EventStatus.NEW_MODIFICATION;
        }
        if (currentlyPublished != null && waitingForReview == null && draft == null) {
            return EventStatus.PUBLISHED;
        }
        return EventStatus.INVALID;
    }
}
