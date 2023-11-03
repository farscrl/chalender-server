package ch.chalender.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document("subscriptions")
@Data
public class Subscription {
    private String id;

    private String name;
    private SubscriptionType type;
    private boolean active = true;

    private List<EventGenre> genres = new ArrayList<>();
    private List<EventRegion> regions = new ArrayList<>();
    private List<EventLanguage> eventLanguages = new ArrayList<>();

    private String searchTerm;

    @JsonIgnore
    private String username;

    @CreatedDate
    @JsonIgnore
    private Instant createdDate;

    @LastModifiedDate
    @JsonIgnore
    private Instant lastModifiedDate;

    @CreatedBy
    @JsonIgnore
    private String createdBy;

    @LastModifiedBy
    @JsonIgnore
    private String lastModifiedBy;

    public enum SubscriptionType {
        INSTANT,
        WEEKLY;
    }
}
