package ch.chalender.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;

import java.time.LocalDate;

@Data
public class EventOccurrence {
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate date;

    private String start;

    private String end;

    @JsonProperty(value="isAllDay")
    private boolean isAllDay = false;

    @JsonProperty(value="isCancelled")
    private boolean isCancelled = false;

    private String occurrenceUid;

    public EventOccurrence() {
        UidGenerator ug = new RandomUidGenerator();
        occurrenceUid = ug.generateUid().getValue();
    }
}
