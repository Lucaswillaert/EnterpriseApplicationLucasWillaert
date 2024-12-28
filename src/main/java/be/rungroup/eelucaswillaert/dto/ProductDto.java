package be.rungroup.eelucaswillaert.dto;

import be.rungroup.eelucaswillaert.model.Tag;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    @NotBlank(message = "Name is mandatory")
    private String name;

    private String description;
    @NotNull(message = "Total stock is mandatory")
    private int totalStock;
    @NotBlank(message = "Photo URL is mandatory")
    private String photoUrl;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Tag> tags = new ArrayList<>();
}
