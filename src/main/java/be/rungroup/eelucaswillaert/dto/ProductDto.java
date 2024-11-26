package be.rungroup.eelucaswillaert.dto;

import be.rungroup.eelucaswillaert.model.Tag;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ProductDto {

    private Long id;
    @NotBlank(message = "Name is mandatory")
    private String name;

    private String description;
    @NotBlank(message = "Total stock is mandatory")
    private int totalStock;
    @NotBlank(message = "Photo URL is mandatory")
    private String photoUrl;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Tag> tags = new ArrayList<>();
}
