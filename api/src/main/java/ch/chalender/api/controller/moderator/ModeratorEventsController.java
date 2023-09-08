package ch.chalender.api.controller.moderator;

import ch.chalender.api.dto.ModerationComment;
import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventFilter;
import ch.chalender.api.model.EventVersion;
import ch.chalender.api.service.EventsService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
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
    public ResponseEntity<Event> acceptChanges(@PathVariable String id, @Valid @RequestBody ModerationComment moderationComment) {
        try {
            Event event = eventsService.acceptChanges(id, moderationComment);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/{id}/refuse")
    public ResponseEntity refuseChanges(@PathVariable String id, @Valid @RequestBody ModerationComment moderationComment) {
        try {
            Event event = eventsService.refuseChanges(id, moderationComment);
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

    @PostMapping("/{id}/change")
    public ResponseEntity changeAndPublish(@PathVariable String id, @Valid @RequestBody EventVersion eventToPublish) {
        try {
            Event event = eventsService.changeAndPublish(id, eventToPublish);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        try {
            eventsService.deleteEvent(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
