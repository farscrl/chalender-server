package ch.chalender.api.service.impl;

import ch.chalender.api.dto.*;
import ch.chalender.api.exception.OAuth2AuthenticationProcessingException;
import ch.chalender.api.exception.UserAlreadyExistAuthenticationException;
import ch.chalender.api.model.User;
import ch.chalender.api.repository.UserRepository;
import ch.chalender.api.security.oauth2.user.OAuth2UserInfo;
import ch.chalender.api.security.oauth2.user.OAuth2UserInfoFactory;
import ch.chalender.api.service.EmailService;
import ch.chalender.api.service.UserService;
import ch.chalender.api.util.GeneralUtils;
import jakarta.mail.MessagingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    Log LOG = LogFactory.getLog(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public User registerNewUser(final SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException {
        if (signUpRequest.getUserID() != null && userRepository.existsById(signUpRequest.getUserID())) {
            throw new UserAlreadyExistAuthenticationException("User with User id " + signUpRequest.getUserID() + " already exist");
        } else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistAuthenticationException("User with email id " + signUpRequest.getEmail() + " already exist");
        }
        User user = buildNewlyRegistratedUser(signUpRequest);
        user = userRepository.save(user);

        try {
            emailService.sendAccountConfirmationEmail(user.getEmail(), user.getFirstName(), user.getEmailConfirmationCode());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public boolean confirmEmailCode(String code) {
        User user = userRepository.findByEmailConfirmationCode(code);
        if (user == null) {
            return false;
        }
        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean resetPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }

        String passwordToken = RandomStringUtils.randomAlphanumeric(32);
        user.setPasswordResetToken(passwordToken);
        userRepository.save(user);

        try {
            emailService.sendPasswordResetEmail(user.getEmail(), user.getFirstName(), passwordToken);
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOG.error(e);
            return false;
        }
        return true;
    }

    @Override
    public boolean redefinePassword(String token, String password) {
        User user = userRepository.findByPasswordResetToken(token);
        if (user == null) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setPasswordResetToken(null);
        userRepository.save(user);
        return true;
    }

    @Override
    public User updateProfile(User user, UpdateProfileRequest updateProfileRequest) {
        user.setFirstName(updateProfileRequest.getFirstName());
        user.setLastName(updateProfileRequest.getLastName());
        user.setOrganisation(updateProfileRequest.getOrganisation());
        return userRepository.save(user);
    }

    @Override
    public boolean updatePassword(User user, UpdatePasswordRequest updatePasswordRequest) {
        if (!passwordEncoder.matches(updatePasswordRequest.getCurrentPassword(), user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    private User buildNewlyRegistratedUser(final SignUpRequest formDTO) {
        User user = new User();
        user.setFirstName(formDTO.getFirstName());
        user.setLastName(formDTO.getLastName());
        user.setOrganisation(formDTO.getOrganisation());
        user.setEmail(formDTO.getEmail());
        user.setPassword(passwordEncoder.encode(formDTO.getPassword()));
        final HashSet<Role> roles = new HashSet<Role>();
        roles.add(Role.ROLE_USER);
        user.setRoles(roles);
        user.setProvider(formDTO.getSocialProvider().getProviderType());
        user.setEnabled(false);
        user.setProviderUserId(formDTO.getProviderUserId());
        user.setCreatedDate(Calendar.getInstance().getTime());
        user.setModifiedDate(Calendar.getInstance().getTime());
        user.setEmailConfirmationCode(RandomStringUtils.randomAlphanumeric(32));
        return user;
    }

    @Override
    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);
        if (StringUtils.isEmpty(oAuth2UserInfo.getName())) {
            throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
        } else if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        SignUpRequest userDetails = toUserRegistrationObject(registrationId, oAuth2UserInfo);
        User user = findUserByEmail(oAuth2UserInfo.getEmail());
        if (user != null) {
            if (!user.getProvider().equals(registrationId) && !user.getProvider().equals(SocialProvider.LOCAL.getProviderType())) {
                throw new OAuth2AuthenticationProcessingException(
                        "Looks like you're signed up with " + user.getProvider() + " account. Please use your " + user.getProvider() + " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(userDetails);
        }

        return LocalUser.create(user, attributes, idToken, userInfo);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFirstName(oAuth2UserInfo.getName());
        return userRepository.save(existingUser);
    }

    private SignUpRequest toUserRegistrationObject(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        return SignUpRequest.getBuilder().addProviderUserID(oAuth2UserInfo.getId()).addFirstName(oAuth2UserInfo.getName()).addEmail(oAuth2UserInfo.getEmail())
                .addSocialProvider(GeneralUtils.toSocialProvider(registrationId)).addPassword("changeit").build();
    }

    @Override
    public Optional<User> findUserById(String id) {
        return userRepository.findById(id);
    }
}
