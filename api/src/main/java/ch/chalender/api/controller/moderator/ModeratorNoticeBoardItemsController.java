package ch.chalender.api.controller.moderator;

import ch.chalender.api.dto.ModerationComment;
import ch.chalender.api.model.ModerationNoticeBoardFilter;
import ch.chalender.api.model.NoticeBoardItem;
import ch.chalender.api.model.NoticeBoardItemVersion;
import ch.chalender.api.service.NoticeBoardService;
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
@RequestMapping("/api/moderator/notices")
public class ModeratorNoticeBoardItemsController {

    @Autowired
    private NoticeBoardService noticeBoardService;

    @GetMapping("")
    @PageableAsQueryParam
    public ResponseEntity<Page<NoticeBoardItem>> listAllNotices(ModerationNoticeBoardFilter filter, @Parameter(hidden = true) Pageable pageable) {
        return ResponseEntity.ok(noticeBoardService.listAllNoticeBoardItem(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeBoardItem> showNoticeBoardItem(@PathVariable String id) {
        return ResponseEntity.ok(noticeBoardService.getNoticeBoardItem(id));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<NoticeBoardItem> acceptChanges(@PathVariable String id, @Valid @RequestBody ModerationComment moderationComment) {
        try {
            NoticeBoardItem item = noticeBoardService.acceptChanges(id, moderationComment);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/{id}/refuse")
    public ResponseEntity refuseChanges(@PathVariable String id, @Valid @RequestBody ModerationComment moderationComment) {
        try {
            NoticeBoardItem item = noticeBoardService.refuseChanges(id, moderationComment);
            if (item == null) {
                return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/{id}/change")
    public ResponseEntity changeAndPublish(@PathVariable String id, @Valid @RequestBody NoticeBoardItemVersion itemToPublish) {
        try {
            NoticeBoardItem item = noticeBoardService.changeAndPublish(id, itemToPublish);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        try {
            noticeBoardService.deleteNoticeBoardItem(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
