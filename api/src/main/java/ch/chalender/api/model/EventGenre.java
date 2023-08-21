package ch.chalender.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("genres")
@Data
public class EventGenre {
    private int id;
    private String name;
    private int order;

    @JsonIgnore
    private boolean isHidden = false;

    public EventGenre(int id, String name, int order, boolean isHidden) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.isHidden = isHidden;
    }
}
