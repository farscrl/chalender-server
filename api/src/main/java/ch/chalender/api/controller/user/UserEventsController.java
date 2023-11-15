package ch.chalender.api.controller.user;

import ch.chalender.api.config.CurrentUser;
import ch.chalender.api.dto.LocalUser;
import ch.chalender.api.model.Event;
import ch.chalender.api.model.ModerationEventsFilter;
import ch.chalender.api.model.User;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/user/events")
public class UserEventsController {

    @Autowired
    private EventsService eventsService;

    @GetMapping("")
    @PageableAsQueryParam
    public ResponseEntity<Page<Event>> listAllEvents(ModerationEventsFilter eventFilter, @Parameter(hidden = true) Pageable pageable, @CurrentUser LocalUser localUser) {
        User user = localUser.getUser();
        return ResponseEntity.ok(eventsService.listAllEventsByUser(eventFilter, user, pageable));
    }
}
