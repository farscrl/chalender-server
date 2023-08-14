package ch.chalender.api.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventOccurrences {
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private boolean isAllDay = false;
    private boolean isCancelled = false;
}
