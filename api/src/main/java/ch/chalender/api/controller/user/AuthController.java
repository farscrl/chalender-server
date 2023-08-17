package ch.chalender.api.controller.user;

import ch.chalender.api.dto.*;
import ch.chalender.api.exception.UserAlreadyExistAuthenticationException;
import ch.chalender.api.security.jwt.TokenProvider;
import ch.chalender.api.service.UserService;
import ch.chalender.api.util.GeneralUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LocalUser localUser = (LocalUser) authentication.getPrincipal();
        UserInfo userInfo = GeneralUtils.buildUserInfo(localUser);
        String jwt = tokenProvider.createToken(authentication, userInfo);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, userInfo));
    }

    @PostMapping("/signup")
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
    public ResponseEntity<?> confirmEmail(String code) {
        boolean valid = userService.confirmEmailCode(code);
        if (valid) {
            return ResponseEntity.ok().body(new ApiResponse(true, "Email confirmed successfully"));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid email confirmation code"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(String email) {
        boolean valid = userService.resetPassword(email);
        if (valid) {
            return ResponseEntity.ok().body(new ApiResponse(true, "Password reset successfully"));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Error during password reset"));
        }
    }

    @PostMapping("/redefine-password")
    public ResponseEntity<?> redefinePassword(String token, String password) {
        boolean valid = userService.redefinePassword(token, password);
        if (valid) {
            return ResponseEntity.ok().body(new ApiResponse(true, "Password reset successfully"));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Error during password reset"));
        }
    }
}
