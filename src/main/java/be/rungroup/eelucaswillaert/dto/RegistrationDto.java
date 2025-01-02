package be.rungroup.eelucaswillaert.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegistrationDto {
    private Long id;
    @NotEmpty
    private Long email;
    @NotEmpty
    private String password;
}
