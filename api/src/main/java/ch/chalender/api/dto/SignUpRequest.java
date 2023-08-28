package ch.chalender.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpRequest {
    private String userID;

    private String providerUserId;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String email;

    private String organisation;

    private SocialProvider socialProvider;

    @Size(min = 8, message = "{Size.userDto.password}")
    private String password;

    public SignUpRequest(String providerUserId, String firstName, String lastName, String email, String password, SocialProvider socialProvider) {
        this.providerUserId = providerUserId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.socialProvider = socialProvider;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String providerUserID;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private SocialProvider socialProvider;

        public Builder addProviderUserID(final String userID) {
            this.providerUserID = userID;
            return this;
        }

        public Builder addFirstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder addLastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder addEmail(final String email) {
            this.email = email;
            return this;
        }

        public Builder addPassword(final String password) {
            this.password = password;
            return this;
        }

        public Builder addSocialProvider(final SocialProvider socialProvider) {
            this.socialProvider = socialProvider;
            return this;
        }

        public SignUpRequest build() {
            return new SignUpRequest(providerUserID, firstName, lastName, email, password, socialProvider);
        }
    }
}
