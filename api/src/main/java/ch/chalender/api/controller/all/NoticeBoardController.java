package ch.chalender.api.controller.all;

import ch.chalender.api.config.CurrentUser;
import ch.chalender.api.converter.NoticeBoardItemConverter;
import ch.chalender.api.dto.LocalUser;
import ch.chalender.api.dto.NoticeBoardItemDto;
import ch.chalender.api.dto.Role;
import ch.chalender.api.model.NoticeBoardFilter;
import ch.chalender.api.model.NoticeBoardItem;
import ch.chalender.api.model.NoticeBoardItemVersion;
import ch.chalender.api.model.PublicationStatus;
import ch.chalender.api.repository.DocumentsRepository;
import ch.chalender.api.repository.ImagesRepository;
import ch.chalender.api.service.NoticeBoardService;
import ch.chalender.api.util.DataUtil;
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
@RequestMapping("/api/notices")
@Tag(name = "Notice Board", description = "List notice board entries")
public class NoticeBoardController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private NoticeBoardService noticeBoardService;

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private DocumentsRepository documentsRepository;


    @GetMapping("")
    @PageableAsQueryParam
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<NoticeBoardItemDto>> listAllNoticeBoardEntries(NoticeBoardFilter noticeBoardFilter, @Parameter(hidden = true) Pageable pageable ) {
        return ResponseEntity.ok(noticeBoardService.getAllNoticeBoardEntries(noticeBoardFilter, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<NoticeBoardItemDto> getNoticeBoardItem(@PathVariable String id, @CurrentUser LocalUser localUser) {
        NoticeBoardItem item = noticeBoardService.getNoticeBoardItem(id);

        if (item == null) {
            return ResponseEntity.notFound().build();
        }

        if (item.getPublicationStatus() != PublicationStatus.PUBLISHED && item.getPublicationStatus() != PublicationStatus.NEW_MODIFICATION) {
            if (!localUser.getUser().getEmail().equals(item.getOwnerEmail()) && !localUser.getUser().getRoles().contains(Role.ROLE_MODERATOR)  && !localUser.getUser().getRoles().contains(Role.ROLE_ADMIN)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }

        return ResponseEntity.ok(NoticeBoardItemConverter.toNoticeBoardItemDto(modelMapper, item));
    }

    @PostMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<NoticeBoardItemDto> createNoticeBoardItem(@Valid @RequestBody NoticeBoardItemDto itemToCreate, @CurrentUser LocalUser localUser) {
        itemToCreate.setImages(DataUtil.updateImages(imagesRepository, itemToCreate.getImages()));
        itemToCreate.setDocuments(DataUtil.updateDocuments(documentsRepository, itemToCreate.getDocuments()));
        NoticeBoardItemVersion version = NoticeBoardItemConverter.toNoticeBoardItemVersion(modelMapper, itemToCreate);
        NoticeBoardItem item = new NoticeBoardItem();

        validateStateAndUpdateNoticeBoardVersionVersion(PublicationStatus.DRAFT, itemToCreate.getStatus(), item, version);

        if (localUser != null) {
            item.setOwnerEmail(localUser.getUser().getEmail());
        } else {
            item.setOwnerEmail(itemToCreate.getContactEmail());
        }

        item = noticeBoardService.createNoticeBoardItem(item);
        return ResponseEntity.ok(NoticeBoardItemConverter.toNoticeBoardItemDto(modelMapper, item));
    }

    @PostMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<NoticeBoardItemDto> updateNoticeBoardItem(@PathVariable String id, @Valid @RequestBody NoticeBoardItemDto itemDto, @CurrentUser LocalUser localUser) {
        NoticeBoardItem itemToModify = noticeBoardService.getNoticeBoardItem(id);

        if (itemToModify == null) {
            return ResponseEntity.notFound().build();
        }

        if (localUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (!localUser.getUser().getEmail().equals(itemToModify.getOwnerEmail()) && !localUser.getUser().getRoles().contains(Role.ROLE_MODERATOR)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        NoticeBoardItemVersion version = NoticeBoardItemConverter.toNoticeBoardItemVersion(modelMapper, itemDto);
        version.setImages(DataUtil.updateImages(imagesRepository, version.getImages()));
        version.setDocuments(DataUtil.updateDocuments(documentsRepository, version.getDocuments()));
        validateStateAndUpdateNoticeBoardVersionVersion(itemToModify.getPublicationStatus(), itemDto.getStatus(), itemToModify, version);

        itemToModify = noticeBoardService.updateNoticeBoardItem(itemToModify);

        return ResponseEntity.ok(NoticeBoardItemConverter.toNoticeBoardItemDto(modelMapper, itemToModify));
    }

    private void validateStateAndUpdateNoticeBoardVersionVersion(PublicationStatus currentPublicationState, PublicationStatus nextState, NoticeBoardItem item, NoticeBoardItemVersion version) throws InvalidStateRequestedException {
        item.getVersions().add(version);

        switch (currentPublicationState) {
            case DRAFT:
                if (nextState == PublicationStatus.DRAFT) {
                    item.setDraft(version);
                    item.setWaitingForReview(null);
                    item.setCurrentlyPublished(null);
                    item.setRejected(null);
                    item.updateCalculatedFields();
                } else if (nextState == PublicationStatus.IN_REVIEW) {
                    item.setWaitingForReview(version);
                    item.setDraft(null);
                    item.setCurrentlyPublished(null);
                    item.setRejected(null);
                    item.updateCalculatedFields();
                } else {
                    throw new InvalidStateRequestedException("Cannot change state from " + currentPublicationState + " to " + nextState);
                }
                break;

            case IN_REVIEW:
            case REJECTED:
                if (nextState == PublicationStatus.IN_REVIEW) {
                    item.setDraft(null);
                    item.setWaitingForReview(version);
                    item.setCurrentlyPublished(null);
                    item.setRejected(null);
                    item.updateCalculatedFields();
                } else {
                    throw new InvalidStateRequestedException("Cannot change state from " + currentPublicationState + " to " + nextState);
                }
                break;

            case PUBLISHED:
            case NEW_MODIFICATION:
                if (nextState == PublicationStatus.NEW_MODIFICATION) {
                    item.setDraft(null);
                    item.setWaitingForReview(version);
                    // item.setCurrentlyPublished(); // do not change
                    item.setRejected(null);
                    item.updateCalculatedFields();
                } else {
                    throw new InvalidStateRequestedException("Cannot change state from " + currentPublicationState + " to " + nextState);
                }
                break;

            default:
                throw new InvalidStateRequestedException("Cannot change state from " + currentPublicationState + " to " + nextState);
        }

        item.updateCalculatedFields();
    }

    public static class InvalidStateRequestedException extends RuntimeException {
        public InvalidStateRequestedException(String message) {
            super(message);
        }
    }
}
