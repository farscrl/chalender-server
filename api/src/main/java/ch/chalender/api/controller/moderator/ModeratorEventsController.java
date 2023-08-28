package ch.chalender.api.controller.moderator;

import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventFilter;
import ch.chalender.api.service.EventsService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@PreAuthorize("hasRole('MODERATOR')")
@RequestMapping("/api/moderator/events")
public class ModeratorEventsController {

    @Autowired
    private EventsService eventsService;

    @GetMapping("")
    @PageableAsQueryParam
    public ResponseEntity<Page<Event>> listAllEvents(EventFilter eventFilter, @Parameter(hidden = true) Pageable pageable) {
        return ResponseEntity.ok(eventsService.listAllEvents(eventFilter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> showEvent(String id) {
        return ResponseEntity.ok(eventsService.getEvent(id));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<Event> acceptChanges(String id) {
        return ResponseEntity.ok(eventsService.acceptChanges(id));
    }

    @PostMapping("/{id}/refuse")
    public ResponseEntity<Event> refuseChanges(String id) {
        return ResponseEntity.ok(eventsService.refuseChanges(id));
    }
}
