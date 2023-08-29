package ch.chalender.api.model;

import ch.chalender.api.dto.Role;
import ch.chalender.api.dto.UserDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Document("users")
@Data
public class User {

    @Id
    private String id;

    private String providerUserId;

    private String email;

    private boolean enabled;

    private String firstName;

    private String lastName;

    private String organisation;

    protected Date createdDate;

    protected Date modifiedDate;

    private String password;

    private String provider;

    private Set<Role> roles;

    private String emailConfirmationCode;

    private String passwordResetToken;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public UserDto toUserDto() {
        return new UserDto(id, firstName, lastName, organisation, email, this.roles.stream().map(Role::name).collect(Collectors.toList()), enabled);
    }
}
