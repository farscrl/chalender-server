package ch.chalender.api.controller.user;

import ch.chalender.api.config.CurrentUser;
import ch.chalender.api.dto.*;
import ch.chalender.api.exception.UserAlreadyExistAuthenticationException;
import ch.chalender.api.model.User;
import ch.chalender.api.security.jwt.TokenProvider;
import ch.chalender.api.service.UserService;
import ch.chalender.api.util.GeneralUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user/auth")
@Tag(name = "Authorization", description = "User authorization and generation")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    TokenProvider tokenProvider;

    @PostMapping("/signin")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LocalUser localUser = (LocalUser) authentication.getPrincipal();
        UserDto userDto = GeneralUtils.buildUserDto(localUser);
        String jwt = tokenProvider.createToken(authentication, userDto);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, userDto));
    }

    @PostMapping("/signup")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            userService.registerNewUser(signUpRequest);
        } catch (UserAlreadyExistAuthenticationException e) {
            log.error("Exception Ocurred", e);
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(new ApiResponse(true, "User registered successfully"));
    }

    @PostMapping("/confirm-email")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> confirmEmail(String code) {
        boolean valid = userService.confirmEmailCode(code);
        if (valid) {
            return ResponseEntity.ok().body(new ApiResponse(true, "Email confirmed successfully"));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid email confirmation code"));
        }
    }

    @PostMapping("/reset-password")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> resetPassword(String email) {
        boolean valid = userService.resetPassword(email);
        if (valid) {
            return ResponseEntity.ok().body(new ApiResponse(true, "Password reset successfully"));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Error during password reset"));
        }
    }

    @PostMapping("/redefine-password")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> redefinePassword(String token, String password) {
        boolean valid = userService.redefinePassword(token, password);
        if (valid) {
            return ResponseEntity.ok().body(new ApiResponse(true, "Password reset successfully"));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Error during password reset"));
        }
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest, @CurrentUser LocalUser localUser) {
        User user = localUser.getUser();

        try {
            userService.updatePassword(user, updatePasswordRequest);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid current password"));
        }

        return ResponseEntity.ok().body(new ApiResponse(true, "Password changed successfully"));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> getMyProfile(@CurrentUser LocalUser user) {
        return ResponseEntity.ok(GeneralUtils.buildUserDto(user));
    }

    @PostMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> setMyProfile(@Valid @RequestBody UserDto userDto, @CurrentUser LocalUser localUser) {
        User user = localUser.getUser();
        user = userService.updateProfile(user, userDto);
        return ResponseEntity.ok(user.toUserDto());
    }

    @PostMapping("/profile/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteMyProfile(@CurrentUser LocalUser localUser, @RequestBody DeleteUserDto deleteUserDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(localUser.getUser().getEmail(), deleteUserDto.getPassword()));
        User user = localUser.getUser();

        if (deleteUserDto.getMode().equals("delete-events")) {
            // TODO: delete events of user
        }

        userService.deleteUser(user.getId());
        return ResponseEntity.ok().build();
    }
}
