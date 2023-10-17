package ch.chalender.api.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("images")
@Data
public class Image {
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
