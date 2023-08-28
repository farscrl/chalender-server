package ch.chalender.api.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateProfileRequest {
    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    private String organisation;
}
