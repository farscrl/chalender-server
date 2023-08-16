package ch.chalender.api.controller.all;

import ch.chalender.api.converter.EventConverter;
import ch.chalender.api.dto.EventDto;
import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventFilter;
import ch.chalender.api.model.EventLookup;
import ch.chalender.api.service.EventLookupService;
import ch.chalender.api.service.EventsService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

@Slf4j
@RestController
@RequestMapping("/api/events")
@Tag(name = "Events", description = "List events")
public class EventsController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EventsService eventsService;

    @Autowired
    private EventLookupService eventLookupService;

    @GetMapping("")
    @PageableAsQueryParam
    public ResponseEntity<Page<EventLookup>> listAllEvents(EventFilter eventFilter, @Parameter(hidden = true) Pageable pageable ) {
        return ResponseEntity.ok(eventLookupService.getAllEvents(eventFilter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEvent(@PathVariable String id) {
        Event event = eventsService.getEvent(id);
        return ResponseEntity.ok(EventConverter.toEventDto(modelMapper, event, EventConverter.EventVersionSelection.CURRENTLY_PUBLISHED));
    }

    @PostMapping("")
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event eventToCreate) {
        if (eventToCreate.getId() != null) {
            return ResponseEntity.badRequest().build();
        }

        if (!eventToCreate.getVersions().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (eventToCreate.getCurrentlyPublished() != null) {
            return ResponseEntity.badRequest().build();
        }

        if (
                (eventToCreate.getDraft() == null && eventToCreate.getWaitingForReview() == null) ||
                (eventToCreate.getDraft() != null && eventToCreate.getWaitingForReview() != null)
        ) {
            return ResponseEntity.badRequest().build();
        }

        Event event = eventsService.createEvent(eventToCreate);
        return ResponseEntity.ok(event);
    }
}
