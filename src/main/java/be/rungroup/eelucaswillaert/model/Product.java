package be.rungroup.eelucaswillaert.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
@Setter
@Getter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
