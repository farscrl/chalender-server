package ch.chalender.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public EventRegion(int id, String name, int order, boolean isHidden) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.isHidden = isHidden;
    }
}
