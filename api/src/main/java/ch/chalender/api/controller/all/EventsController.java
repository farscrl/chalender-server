package ch.chalender.api.controller.all;

import ch.chalender.api.config.CurrentUser;
import ch.chalender.api.converter.EventConverter;
import ch.chalender.api.dto.EventDto;
import ch.chalender.api.dto.LocalUser;
import ch.chalender.api.dto.Role;
import ch.chalender.api.model.*;
import ch.chalender.api.service.EventLookupService;
import ch.chalender.api.service.EventsService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<EventLookup>> listAllEvents(EventFilter eventFilter, @Parameter(hidden = true) Pageable pageable ) {
        return ResponseEntity.ok(eventLookupService.getAllEvents(eventFilter, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EventDto> getEvent(@PathVariable String id) {
        Event event = eventsService.getEvent(id);

        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EventConverter.toEventDto(modelMapper, event));
    }

    @PostMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventDto eventToCreate, @CurrentUser LocalUser localUser) {
        EventVersion version = EventConverter.toEventVersion(modelMapper, eventToCreate);
        Event event = new Event();

        validateState(EventStatus.DRAFT, eventToCreate.getStatus(), event, version);

        if (localUser != null) {
            event.setOwnerEmail(localUser.getUser().getEmail());
        } else {
            event.setOwnerEmail(eventToCreate.getContactEmail());
        }

        event = eventsService.createEvent(event);
        return ResponseEntity.ok(EventConverter.toEventDto(modelMapper, event));
    }

    @PostMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EventDto> updateEvent(@PathVariable String id, @Valid @RequestBody EventDto eventDto, @CurrentUser LocalUser localUser) {
        Event eventToModify = eventsService.getEvent(id);

        if (eventToModify == null) {
            return ResponseEntity.notFound().build();
        }

        if (localUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (!localUser.getUser().getEmail().equals(eventToModify.getOwnerEmail()) && !localUser.getUser().getRoles().contains(Role.ROLE_MODERATOR)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        EventVersion version = EventConverter.toEventVersion(modelMapper, eventDto);
        validateState(eventToModify.getEventStatus(), eventDto.getStatus(), eventToModify, version);

        eventToModify = eventsService.updateEvent(eventToModify);

        return ResponseEntity.ok(EventConverter.toEventDto(modelMapper, eventToModify));
    }

    private void validateState(EventStatus currentEventState, EventStatus nextState, Event event, EventVersion version) throws InvalidStateRequestedException {
        switch (currentEventState) {
            case DRAFT:
                if (nextState == EventStatus.DRAFT) {
                    event.setDraft(version);
                } else if (nextState == EventStatus.IN_REVIEW) {
                    event.setWaitingForReview(version);
                } else {
                    throw new InvalidStateRequestedException("Cannot change state from " + currentEventState + " to " + nextState);
                }
                break;

            case IN_REVIEW:
            case REJECTED:
                if (nextState == EventStatus.IN_REVIEW) {
                    event.setWaitingForReview(version);
                } else {
                    throw new InvalidStateRequestedException("Cannot change state from " + currentEventState + " to " + nextState);
                }
                break;

            case PUBLISHED:
            case NEW_MODIFICATION:
                if (nextState == EventStatus.NEW_MODIFICATION) {
                    event.setWaitingForReview(version);
                } else {
                    throw new InvalidStateRequestedException("Cannot change state from " + currentEventState + " to " + nextState);
                }
                break;

            default:
                throw new InvalidStateRequestedException("Cannot change state from " + currentEventState + " to " + nextState);
        }
        event.getVersions().add(version);
    }

    public static class InvalidStateRequestedException extends RuntimeException {
        public InvalidStateRequestedException(String message) {
            super(message);
        }
    }
}
