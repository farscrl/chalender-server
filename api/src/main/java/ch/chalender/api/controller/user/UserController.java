package ch.chalender.api.controller.user;

import ch.chalender.api.config.CurrentUser;
import ch.chalender.api.dto.LocalUser;
import ch.chalender.api.util.GeneralUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "User related data")
public class UserController {

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUser(@CurrentUser LocalUser user) {
        return ResponseEntity.ok(GeneralUtils.buildUserInfo(user));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getContent() {
        return ResponseEntity.ok("Public content goes here");
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserContent() {
        return ResponseEntity.ok("User content goes here");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminContent() {
        return ResponseEntity.ok("Admin content goes here");
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> getModeratorContent() {
        return ResponseEntity.ok("Moderator content goes here");
    }
}
