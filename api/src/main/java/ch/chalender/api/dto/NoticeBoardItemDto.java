package ch.chalender.api.dto;

import ch.chalender.api.model.Document;
import ch.chalender.api.model.Image;
import ch.chalender.api.model.NoticeBoardGenre;
import ch.chalender.api.model.PublicationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class NoticeBoardItemDto {
    private String id;
    private PublicationStatus status;
    private String contactEmail;

    private String title;
    private List<NoticeBoardGenre> genres = new ArrayList<>();
    private String description;
    private String contactData;
    private List<Image> images = new ArrayList<>();
    private List<Document> documents = new ArrayList<>();
    private boolean acceptTerms;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate publicationDate;
}
