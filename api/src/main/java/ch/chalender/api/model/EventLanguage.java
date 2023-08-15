package ch.chalender.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("languages")
@Data
public class EventLanguage {
    private String id;
    private String name;

    @JsonIgnore
    private boolean isHidden = false;
}
