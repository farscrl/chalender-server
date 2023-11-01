package ch.chalender.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class EventFilter {
    private List<Integer> genres = new ArrayList<>();
    private List<Integer> regions = new ArrayList<>();
    private List<String> eventLanguages = new ArrayList<>();

    @JsonFormat(pattern="dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    private String searchTerm;
}
