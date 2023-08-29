package ch.chalender.api.controller.admin;

import ch.chalender.api.dto.UserDto;
import ch.chalender.api.model.UserFilter;
import ch.chalender.api.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    @PageableAsQueryParam
    public ResponseEntity<Page<UserDto>> listAllUsers(UserFilter userFilter, @Parameter(hidden = true) Pageable pageable) {
        return ResponseEntity.ok(userService.listAllUsers(userFilter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> showUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
