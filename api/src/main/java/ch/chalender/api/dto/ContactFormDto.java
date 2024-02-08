package ch.chalender.api.dto;

import lombok.Data;

@Data
public class ContactFormDto {
    private String name;
    private String email;
    private String phone;
    private String type;
    private String message;
}
