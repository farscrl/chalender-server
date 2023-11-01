package ch.chalender.api.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

@org.springframework.data.mongodb.core.mapping.Document("files")
@Data
public class Document {
    private String id;

    @Indexed
    private boolean used;

    private String originalName;

    @JsonIgnore
    private String path;

    @CreatedDate
    @JsonIgnore
    private Instant createdDate;

    @LastModifiedDate
    @JsonIgnore
    private Instant lastModifiedDate;

    @CreatedBy
    @JsonIgnore
    private String createdBy;

    @LastModifiedBy
    @JsonIgnore
    private String lastModifiedBy;

    public String getUrl() {
        return "https://chalender.b-cdn.net/" + path;
    }
}
