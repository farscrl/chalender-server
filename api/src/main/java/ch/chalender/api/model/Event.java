package ch.chalender.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document("events")
@Data
public class Event {
    private String id;

    // calculated fields
    private PublicationStatus publicationStatus;
    @JsonIgnore private LocalDate firstOccurrenceDate;
    @JsonIgnore private LocalDate lastOccurrenceDate;
    @JsonIgnore private String title;

    private EventVersion draft;
    private EventVersion currentlyPublished;
    private EventVersion waitingForReview;
    private EventVersion rejected;

    private List<EventVersion> versions = new ArrayList<>();

    private String ownerEmail;

    @Transient
    private String contactEmail;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @CreatedBy
    @JsonIgnore
    private String createdBy;

    @LastModifiedBy
    @JsonIgnore
    private String lastModifiedBy;

    public void updateCalculatedEventFields() {
        this.publicationStatus = calculateEventStatus();
        calculateEventDatesAndTitle();
    }

    public void setPublicationStatus(PublicationStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    public PublicationStatus getPublicationStatus() {
        if (publicationStatus != null) {
            return publicationStatus;
        }

        return calculateEventStatus();
    }

    private PublicationStatus calculateEventStatus() {
        if (draft != null && currentlyPublished == null && waitingForReview == null && rejected == null) {
            return PublicationStatus.DRAFT;
        }
        if (waitingForReview != null && currentlyPublished == null && draft == null && rejected == null) {
            return PublicationStatus.IN_REVIEW;
        }
        if (currentlyPublished != null && waitingForReview != null && draft == null && rejected == null) {
            return PublicationStatus.NEW_MODIFICATION;
        }
        if (currentlyPublished != null && waitingForReview == null && draft == null && rejected == null) {
            return PublicationStatus.PUBLISHED;
        }
        if (rejected != null && currentlyPublished == null && waitingForReview == null && draft == null) {
            return PublicationStatus.REJECTED;
        }
        return PublicationStatus.INVALID;
    }

    private void calculateEventDatesAndTitle() {
        EventVersion version = null;
        if (waitingForReview != null) {
            version = waitingForReview;
        } else if (currentlyPublished != null) {
            version = currentlyPublished;
        } else if (rejected != null) {
            version = rejected;
        } else if (draft != null) {
            version = draft;
        }

        if (version == null) {
            return;
        }

        firstOccurrenceDate = version.getOccurrences().stream()
                .map(EventOccurrence::getDate)
                .min(LocalDate::compareTo)
                .orElse(null);
        lastOccurrenceDate = version.getOccurrences().stream()
                .map(EventOccurrence::getDate)
                .max(LocalDate::compareTo)
                .orElse(null);
        title = version.getTitle();
    }
}
