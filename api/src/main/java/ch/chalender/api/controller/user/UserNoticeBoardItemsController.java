package ch.chalender.api.controller.user;

import ch.chalender.api.config.CurrentUser;
import ch.chalender.api.dto.LocalUser;
import ch.chalender.api.model.ModerationNoticeBoardFilter;
import ch.chalender.api.model.NoticeBoardItem;
import ch.chalender.api.model.User;
import ch.chalender.api.service.NoticeBoardService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/user/notices")
public class UserNoticeBoardItemsController {

    @Autowired
    private NoticeBoardService noticeBoardService;

    @GetMapping("")
    @PageableAsQueryParam
    public ResponseEntity<Page<NoticeBoardItem>> listAllNoticeBoardItems(ModerationNoticeBoardFilter noticeBoardFilter, @Parameter(hidden = true) Pageable pageable, @CurrentUser LocalUser localUser) {
        User user = localUser.getUser();
        return ResponseEntity.ok(noticeBoardService.listAllNoticeBoardItemsByUser(noticeBoardFilter, user, pageable));
    }
}
