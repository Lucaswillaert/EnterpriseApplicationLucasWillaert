package be.rungroup.eelucaswillaert.dto;

import be.rungroup.eelucaswillaert.model.Tag;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    private transient MultipartFile photo; // Markeer als transient voor bestandshantering

    private byte[] photoBytes; // Voor opslag in de database


    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Tag> tags = new ArrayList<>();


    public byte[] getPhotoBytes()  {
        try{
            return photo != null ? photo.getBytes() : null;
        } catch (IOException e) {
            System.err.println("Error processing photo bytes: " + e.getMessage());
            return new byte[0]; // Fallback
        }

    }
}
