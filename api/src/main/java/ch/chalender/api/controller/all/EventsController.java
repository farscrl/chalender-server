package ch.chalender.api.controller.all;

import ch.chalender.api.config.CurrentUser;
import ch.chalender.api.converter.EventConverter;
import ch.chalender.api.dto.EventDto;
import ch.chalender.api.dto.LocalUser;
import ch.chalender.api.dto.Role;
import ch.chalender.api.model.*;
import ch.chalender.api.repository.DocumentsRepository;
import ch.chalender.api.repository.ImagesRepository;
import ch.chalender.api.service.EventLookupService;
import ch.chalender.api.service.EventsService;
import ch.chalender.api.util.DataUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private DocumentsRepository documentsRepository;


    @GetMapping("")
    @PageableAsQueryParam
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<EventLookup>> listAllEvents(EventFilter eventFilter, @Parameter(hidden = true) Pageable pageable ) {
        return ResponseEntity.ok(eventLookupService.getAllEvents(eventFilter, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EventDto> getEvent(@PathVariable String id, @CurrentUser LocalUser localUser) {
        Event event = eventsService.getEvent(id);

        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        boolean forbiddenToSeeDetails = false;
        if (
                localUser == null  ||
                (!localUser.getUser().getEmail().equals(event.getOwnerEmail()) && !localUser.getUser().getRoles().contains(Role.ROLE_MODERATOR)  && !localUser.getUser().getRoles().contains(Role.ROLE_ADMIN))
        ) {
            forbiddenToSeeDetails = true;
        }

        if (event.getPublicationStatus() != PublicationStatus.PUBLISHED && event.getPublicationStatus() != PublicationStatus.NEW_MODIFICATION) {
            if (forbiddenToSeeDetails) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }

        return ResponseEntity.ok(EventConverter.toEventDto(modelMapper, event, !forbiddenToSeeDetails));
    }

    @GetMapping("/{id}/ics/{uid}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Resource> getEventIcs(@PathVariable String id, @PathVariable String uid, @CurrentUser LocalUser localUser) {
        Event event = eventsService.getEvent(id);

        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        if (event.getPublicationStatus() != PublicationStatus.PUBLISHED && event.getPublicationStatus() != PublicationStatus.NEW_MODIFICATION) {
            if (!localUser.getUser().getEmail().equals(event.getOwnerEmail()) && !localUser.getUser().getRoles().contains(Role.ROLE_MODERATOR)  && !localUser.getUser().getRoles().contains(Role.ROLE_ADMIN)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }

        Resource resource = eventsService.getEventIcs(id, uid);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mycalendar.ics");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        return ResponseEntity.ok().headers(header).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

    @PostMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventDto eventToCreate, @CurrentUser LocalUser localUser) {
        eventToCreate.setImages(DataUtil.updateImages(imagesRepository, eventToCreate.getImages()));
        eventToCreate.setDocuments(DataUtil.updateDocuments(documentsRepository, eventToCreate.getDocuments()));
        EventVersion version = EventConverter.toEventVersion(modelMapper, eventToCreate);
        Event event = new Event();

        validateStateAndUpdateEventVersion(PublicationStatus.DRAFT, eventToCreate.getStatus(), event, version);

        if (localUser != null) {
            event.setOwnerEmail(localUser.getUser().getEmail());
        } else {
            event.setOwnerEmail(eventToCreate.getContactEmail());
        }

        event = eventsService.createEvent(event);
        return ResponseEntity.ok(EventConverter.toEventDto(modelMapper, event, true));
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
        version.setImages(DataUtil.updateImages(imagesRepository, version.getImages()));
        version.setDocuments(DataUtil.updateDocuments(documentsRepository, version.getDocuments()));
        validateStateAndUpdateEventVersion(eventToModify.getPublicationStatus(), eventDto.getStatus(), eventToModify, version);

        eventToModify = eventsService.updateEvent(eventToModify);

        return ResponseEntity.ok(EventConverter.toEventDto(modelMapper, eventToModify, true));
    }

    private void validateStateAndUpdateEventVersion(PublicationStatus currentEventState, PublicationStatus nextState, Event event, EventVersion version) throws InvalidStateRequestedException {
        event.getVersions().add(version);

        switch (currentEventState) {
            case DRAFT:
                if (nextState == PublicationStatus.DRAFT) {
                    event.setDraft(version);
                    event.setWaitingForReview(null);
                    event.setCurrentlyPublished(null);
                    event.setRejected(null);
                    event.updateCalculatedEventFields();
                } else if (nextState == PublicationStatus.IN_REVIEW) {
                    event.setWaitingForReview(version);
                    event.setDraft(null);
                    event.setCurrentlyPublished(null);
                    event.setRejected(null);
                    event.updateCalculatedEventFields();
                } else {
                    throw new InvalidStateRequestedException("Cannot change state from " + currentEventState + " to " + nextState);
                }
                break;

            case IN_REVIEW:
            case REJECTED:
                if (nextState == PublicationStatus.IN_REVIEW) {
                    event.setDraft(null);
                    event.setWaitingForReview(version);
                    event.setCurrentlyPublished(null);
                    event.setRejected(null);
                    event.updateCalculatedEventFields();
                } else {
                    throw new InvalidStateRequestedException("Cannot change state from " + currentEventState + " to " + nextState);
                }
                break;

            case PUBLISHED:
            case NEW_MODIFICATION:
                if (nextState == PublicationStatus.NEW_MODIFICATION) {
                    event.setDraft(null);
                    event.setWaitingForReview(version);
                    // event.setCurrentlyPublished(); // do not change
                    event.setRejected(null);
                    event.updateCalculatedEventFields();
                } else {
                    throw new InvalidStateRequestedException("Cannot change state from " + currentEventState + " to " + nextState);
                }
                break;

            default:
                throw new InvalidStateRequestedException("Cannot change state from " + currentEventState + " to " + nextState);
        }

        event.updateCalculatedEventFields();
    }

    public static class InvalidStateRequestedException extends RuntimeException {
        public InvalidStateRequestedException(String message) {
            super(message);
        }
    }
}
