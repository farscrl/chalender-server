package ch.chalender.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {
    private String id;

    @NotEmpty
    private String email;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    private String organisation;

    private String phone;

    List<String> roles = new ArrayList<>();

    @JsonProperty(value="isActive")
    boolean isActive = false;

    public UserDto() {
    }

    public UserDto(String id, String firstName, String lastName, String organisation, String phone, String email, List<String> roles, boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organisation = organisation;
        this.phone = phone;
        this.email = email;
        this.roles = roles;
        this.isActive = isActive;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
