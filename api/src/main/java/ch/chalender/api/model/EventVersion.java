package ch.chalender.api.model;

import ch.chalender.api.dto.ModerationComment;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

@Data
public class EventVersion {
      private String title;
      private List<EventGenre> genres = new ArrayList<>();
      private String description;
      private String location;
      private String address;
      private List<EventOccurrence> occurrences = new ArrayList<>();
      private List<EventRegion> regions = new ArrayList<>();
      private List<Image> images = new ArrayList<>();
      private List<EventLanguage> eventLanguages = new ArrayList<>();
      private boolean onlineOnly;
      private boolean acceptTerms;
      private String organiser;
      private String pricing;
      private String link;
      private String contact;

      @Transient
      private ModerationComment moderationComment;
}
