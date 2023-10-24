package ch.chalender.api.model;

import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("events")
@Data
public class Event {
    private String id;

    private EventVersion draft;
    private EventVersion currentlyPublished;
    private EventVersion waitingForReview;
    private EventVersion rejected;

    private List<EventVersion> versions = new ArrayList<>();

    private String ownerEmail;

    @Transient
    private String contactEmail;

    public EventStatus getEventStatus() {
        if (draft != null && currentlyPublished == null && waitingForReview == null && rejected == null) {
            return EventStatus.DRAFT;
        }
        if (waitingForReview != null && currentlyPublished == null && draft == null && rejected == null) {
            return EventStatus.IN_REVIEW;
        }
        if (currentlyPublished != null && waitingForReview != null && draft == null && rejected == null) {
            return EventStatus.NEW_MODIFICATION;
        }
        if (currentlyPublished != null && waitingForReview == null && draft == null && rejected == null) {
            return EventStatus.PUBLISHED;
        }
        if (rejected != null && currentlyPublished == null && waitingForReview == null && draft == null) {
            return EventStatus.REJECTED;
        }
        return EventStatus.INVALID;
    }
}
