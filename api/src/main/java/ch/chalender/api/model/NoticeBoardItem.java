package ch.chalender.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document("notice_board")
@Data
public class NoticeBoardItem {
    private String id;

    // calculated fields
    private PublicationStatus publicationStatus;
    @JsonIgnore private String title;

    private NoticeBoardItemVersion draft;
    private NoticeBoardItemVersion currentlyPublished;
    private NoticeBoardItemVersion waitingForReview;
    private NoticeBoardItemVersion rejected;

    private List<NoticeBoardItemVersion> versions = new ArrayList<>();

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

    public void updateCalculatedFields() {
        this.publicationStatus = calculateStatus();
        calculateEventDatesAndTitle();
    }

    public void setPublicationStatus(PublicationStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    public PublicationStatus getPublicationStatus() {
        if (publicationStatus != null) {
            return publicationStatus;
        }

        return calculateStatus();
    }

    private PublicationStatus calculateStatus() {
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
        NoticeBoardItemVersion version = null;
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

        title = version.getTitle();
    }
}
