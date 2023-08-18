package ch.chalender.api.service;

import ch.chalender.api.dto.LocalUser;
import ch.chalender.api.dto.SignUpRequest;
import ch.chalender.api.dto.UpdatePasswordRequest;
import ch.chalender.api.dto.UpdateProfileRequest;
import ch.chalender.api.exception.UserAlreadyExistAuthenticationException;
import ch.chalender.api.model.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;
import java.util.Optional;

public interface UserService {
    public User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException;

    public boolean confirmEmailCode(String code);

    public boolean resetPassword(String email);

    public boolean redefinePassword(String token, String password);

    public User updateProfile(User user, UpdateProfileRequest updateProfileRequest);

    public boolean updatePassword(User user, UpdatePasswordRequest updatePasswordRequest);

    User findUserByEmail(String email);

    Optional<User> findUserById(String id);

    LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);
}
