package ch.chalender.api.controller.moderator;

import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventFilter;
import ch.chalender.api.service.EventsService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

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
    public ResponseEntity<Event> showEvent(@PathVariable String id) {
        return ResponseEntity.ok(eventsService.getEvent(id));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<Event> acceptChanges(@PathVariable String id) {
        try {
            Event event = eventsService.acceptChanges(id);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/{id}/refuse")
    public ResponseEntity refuseChanges(@PathVariable String id) {
        try {
            Event event = eventsService.refuseChanges(id);
            if (event == null) {
                return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
