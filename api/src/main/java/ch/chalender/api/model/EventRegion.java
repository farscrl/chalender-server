package ch.chalender.api.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("regions")
@Data
public class EventRegion {
    private String id;
    private String name;
}
