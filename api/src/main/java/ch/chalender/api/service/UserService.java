package ch.chalender.api.service;

import ch.chalender.api.dto.LocalUser;
import ch.chalender.api.dto.SignUpRequest;
import ch.chalender.api.dto.UpdatePasswordRequest;
import ch.chalender.api.dto.UserDto;
import ch.chalender.api.exception.UserAlreadyExistAuthenticationException;
import ch.chalender.api.model.User;
import ch.chalender.api.model.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;
import java.util.Optional;

public interface UserService {
    public User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException;

    public boolean confirmEmailCode(String code);

    public boolean resetPassword(String email);

    public boolean redefinePassword(String token, String password);

    public User updateProfile(User user, UserDto userDto);

    public void updatePassword(User user, UpdatePasswordRequest updatePasswordRequest);

    public User findUserByEmail(String email);

    public Optional<User> findUserById(String id);

    public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);

    public Page<UserDto> listAllUsers(UserFilter userFilter, Pageable pageable);

    public UserDto getUser(String id);

    public UserDto updateUser(String id, UserDto userDto);

    public void deleteUser(String id, boolean deleteEvents);
}
