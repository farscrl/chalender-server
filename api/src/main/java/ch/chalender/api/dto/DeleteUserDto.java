package ch.chalender.api.dto;

import lombok.Data;

@Data
public class DeleteUserDto {

    private String password;

    private String mode;
}
