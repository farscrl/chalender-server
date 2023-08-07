package ch.chalender.api.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("genres")
@Data
public class EventGenre {
    private String id;
    private String name;
}
