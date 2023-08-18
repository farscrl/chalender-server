package ch.chalender.api.dto;

import lombok.Value;

import java.util.List;

@Value
public class UserInfo {
    private String id, organisation, displayName, email;
    private List<String> roles;

    public UserInfo(String id, String displayName, String organisation, String email, List<String> roles) {
        this.id = id;
        this.displayName = displayName;
        this.organisation = organisation;
        this.email = email;
        this.roles = roles;
    }
}
