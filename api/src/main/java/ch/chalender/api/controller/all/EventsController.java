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

import java.util.ArrayList;
import java.util.List;

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
        eventToCreate.setImages(updateImages(eventToCreate.getImages()));
        eventToCreate.setDocuments(updateDocuments(eventToCreate.getDocuments()));
        EventVersion version = EventConverter.toEventVersion(modelMapper, eventToCreate);
        Event event = new Event();

        validateStateAndUpdateEventVersion(EventStatus.DRAFT, eventToCreate.getStatus(), event, version);

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
        version.setImages(updateImages(version.getImages()));
        version.setDocuments(updateDocuments(version.getDocuments()));
        validateStateAndUpdateEventVersion(eventToModify.getEventStatus(), eventDto.getStatus(), eventToModify, version);

        eventToModify = eventsService.updateEvent(eventToModify);

        return ResponseEntity.ok(EventConverter.toEventDto(modelMapper, eventToModify));
    }

    private void validateStateAndUpdateEventVersion(EventStatus currentEventState, EventStatus nextState, Event event, EventVersion version) throws InvalidStateRequestedException {
        event.getVersions().add(version);

        switch (currentEventState) {
            case DRAFT:
                if (nextState == EventStatus.DRAFT) {
                    event.setDraft(version);
                    event.setWaitingForReview(null);
                    event.setCurrentlyPublished(null);
                    event.setRejected(null);
                } else if (nextState == EventStatus.IN_REVIEW) {
                    event.setWaitingForReview(version);
                    event.setDraft(null);
                    event.setCurrentlyPublished(null);
                    event.setRejected(null);
                } else {
                    throw new InvalidStateRequestedException("Cannot change state from " + currentEventState + " to " + nextState);
                }
                break;

            case IN_REVIEW:
            case REJECTED:
                if (nextState == EventStatus.IN_REVIEW) {
                    event.setDraft(null);
                    event.setWaitingForReview(version);
                    event.setCurrentlyPublished(null);
                    event.setRejected(null);
                } else {
                    throw new InvalidStateRequestedException("Cannot change state from " + currentEventState + " to " + nextState);
                }
                break;

            case PUBLISHED:
            case NEW_MODIFICATION:
                if (nextState == EventStatus.NEW_MODIFICATION) {
                    event.setDraft(null);
                    event.setWaitingForReview(version);
                    // event.setCurrentlyPublished(); // do not change
                    event.setRejected(null);
                } else {
                    throw new InvalidStateRequestedException("Cannot change state from " + currentEventState + " to " + nextState);
                }
                break;

            default:
                throw new InvalidStateRequestedException("Cannot change state from " + currentEventState + " to " + nextState);
        }
    }

    /**
     * Update the images in the dto with the ones from the database.
     * This is used, as not all data regarding images is exposed over the API.
     *
     * @param images The list of the images to update
     * @return The updated images-list
     */
    private List<Image> updateImages(List<Image> images) {
        List<Image> imagesFound = new ArrayList<>();

        for (Image image : images) {
            Image imageFound = imagesRepository.findById(image.getId()).orElse(null);
            if (imageFound != null) {
                imagesFound.add(imageFound);
            }
        }

        return imagesFound;
    }

    /**
     * Update the documents in the dto with the ones from the database.
     * This is used, as not all data regarding documents is exposed over the API.
     *
     * @param documents The list of the documents to update
     * @return The updated documents-list
     */
    private List<Document> updateDocuments(List<Document> documents) {
        List<Document> documentsFound = new ArrayList<>();

        for (Document document : documents) {
            Document documentFound = documentsRepository.findById(document.getId()).orElse(null);
            if (documentFound != null) {
                documentsFound.add(documentFound);
            }
        }

        return documentsFound;
    }

    public static class InvalidStateRequestedException extends RuntimeException {
        public InvalidStateRequestedException(String message) {
            super(message);
        }
    }
}
