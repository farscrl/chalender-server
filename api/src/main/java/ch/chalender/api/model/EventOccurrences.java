package ch.chalender.api.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventOccurrences {
    private LocalDateTime start;
    private LocalDateTime end;
}
