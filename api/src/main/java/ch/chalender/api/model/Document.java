package ch.chalender.api.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

@org.springframework.data.mongodb.core.mapping.Document("files")
@Data
public class Document {
    private String id;

    @Indexed
    private boolean used;

    private String originalName;

    @JsonIgnore
    private String path;

    public String getUrl() {
        return "https://chalender.b-cdn.net/" + path;
    }
}
