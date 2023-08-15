package ch.chalender.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventOccurrence {
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate date;

    @JsonFormat(pattern="HH:mm")
    private LocalTime start;

    @JsonFormat(pattern="HH:mm")
    private LocalTime end;

    @JsonProperty(value="isAllDay")
    private boolean isAllDay = false;

    @JsonProperty(value="isCancelled")
    private boolean isCancelled = false;
}
