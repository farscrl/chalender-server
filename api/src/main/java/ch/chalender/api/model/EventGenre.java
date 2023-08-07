package ch.chalender.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("genres")
@Data
public class EventGenre {
    private int id;
    private String name;

    @JsonIgnore
    private boolean isHidden = false;
}
