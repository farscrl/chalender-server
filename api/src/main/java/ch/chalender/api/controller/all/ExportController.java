package ch.chalender.api.controller.all;

import ch.chalender.api.model.EventFilter;
import ch.chalender.api.model.EventLookup;
import ch.chalender.api.service.EventLookupService;
import com.opencsv.CSVWriter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/export")
@Tag(name = "Data export", description = "Export chalender data API")
public class ExportController {
    @Autowired
    private EventLookupService eventLookupService;

    @GetMapping("csv")
    @PageableAsQueryParam
    @PreAuthorize("permitAll()")
    public void exportCsv(HttpServletResponse response) throws IOException {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate tomorrowNextWeek = today.plusWeeks(1);
        
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"date", "time", "title", "location", "image", "link"});

        EventFilter eventFilter = new EventFilter();
        eventFilter.setDate(tomorrow);
        List<EventLookup> events = eventLookupService.getEventsInDateRange(eventFilter, tomorrow, tomorrowNextWeek);
        for (EventLookup event : events) {
            String time = "tuttadi";
            if (!event.isAllDay()) {
                time = formatTimeWithDot(event.getStart());
            }
            data.add(new String[]{
                    formatDate(event.getDate()),
                    time,
                    event.getTitle(),
                    event.getLocation(),
                    event.getImageUrl(),
                    "https://chalender.ch/" + event.getId()
            });
        }

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"occurrenzas-chalender.csv\"");

        try (CSVWriter writer = new CSVWriter(response.getWriter())) {
            writer.writeAll(data);
        }
    }

    private String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(dateFormatter);
    }

    private String formatTimeWithDot(String time) {
        if (time == null) {
            return null;
        }

        return time.replace(":", ".");
    }
}
