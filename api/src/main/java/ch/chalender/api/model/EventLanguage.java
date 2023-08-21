package ch.chalender.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public EventLanguage(String id, String name, int order, boolean isHidden) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.isHidden = isHidden;
    }
}
