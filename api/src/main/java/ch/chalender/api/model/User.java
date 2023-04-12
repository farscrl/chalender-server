package ch.chalender.api.model;

import ch.chalender.api.dto.Role;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Set;

@Document("users")
@Data
public class User {

    @Id
    private String id;

    private String providerUserId;

    private String email;

    private boolean enabled;

    private String displayName;

    protected Date createdDate;

    protected Date modifiedDate;

    private String password;

    private String provider;

    private Set<Role> roles;
}
