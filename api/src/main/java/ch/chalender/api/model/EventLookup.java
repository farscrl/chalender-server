package ch.chalender.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Document("events_lookup")
@Data
public class EventLookup {
    @JsonIgnore
    private String id;

    @Indexed
    private String eventId;

    private String title;
    private String description;
    private List<EventGenre> genres = new ArrayList<>();
    private String location;
    private List<EventRegion> regions = new ArrayList<>();
    private List<EventLanguage> eventLanguages = new ArrayList<>();
    private String address;
    private String organiser;
    private boolean onlineOnly;

    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate date;

    @JsonFormat(pattern="HH:mm")
    private LocalTime start;

    @JsonFormat(pattern="HH:mm")
    private LocalTime end;

    @JsonProperty(value="isAllDay")
    private boolean isAllDay = false;

    @JsonProperty(value="isCancelled")
    private boolean isCancelled = false;

    private String imageUrl;
}
