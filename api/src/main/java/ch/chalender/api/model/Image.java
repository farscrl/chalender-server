package ch.chalender.api.model;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("images")
@Data
public class Image {
    private String id;
}
