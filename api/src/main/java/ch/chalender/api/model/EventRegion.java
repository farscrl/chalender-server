package ch.chalender.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("regions")
@Data
public class EventRegion {
    private int id;
    private String name;
    private int order;

    @JsonIgnore
    private boolean isHidden = false;

    @JsonCreator
    public EventRegion(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("order") int order, @JsonProperty("isHidden") boolean isHidden) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.isHidden = isHidden;
    }
}
