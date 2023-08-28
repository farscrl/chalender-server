package ch.chalender.api.dto;

import lombok.Value;

import java.util.List;

@Value
public class UserInfo {
    private String id, organisation, firstName, lastName, email;
    private List<String> roles;

    public UserInfo(String id, String firstName, String lastName, String organisation, String email, List<String> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organisation = organisation;
        this.email = email;
        this.roles = roles;
    }
}
