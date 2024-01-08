package ch.chalender.api.model;

import ch.chalender.api.dto.ModerationComment;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

@Data
public class NoticeBoardItemVersion {
      private String title;
      private List<NoticeBoardGenre> genres = new ArrayList<>();
      private String description;
      private String contactData;
      private List<Image> images = new ArrayList<>();
      private List<Document> documents = new ArrayList<>();
      private boolean acceptTerms;

      @Transient
      private ModerationComment moderationComment;
}
