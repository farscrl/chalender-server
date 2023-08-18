package ch.chalender.api.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatePasswordRequest {
    @NotEmpty
    private String currentPassword;

    private String newPassword;
}
