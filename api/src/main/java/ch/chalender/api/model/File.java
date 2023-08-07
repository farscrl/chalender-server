package ch.chalender.api.model;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("files")
@Data
public class File {
    private String id;
}
