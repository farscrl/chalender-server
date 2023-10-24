package ch.chalender.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("languages")
@Data
public class EventLanguage {
    private String id;
    private String name;
    private int order;

    @JsonIgnore
    private boolean isHidden = false;

    @JsonCreator
    public EventLanguage(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("order") int order, @JsonProperty("isHidden") boolean isHidden) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.isHidden = isHidden;
    }
}
