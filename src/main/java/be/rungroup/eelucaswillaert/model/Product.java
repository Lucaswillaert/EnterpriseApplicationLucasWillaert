package be.rungroup.eelucaswillaert.model;


import jakarta.persistence.*;

import lombok.*;
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

    private String name;

    private String description;

    private int totalStock;

    private String photoUrl;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Tag> tags = new ArrayList<>();

    // Make the constructor public
    public void ProductDto(Long id, String name, String description, int totalStock, String photoUrl, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalStock = totalStock;
        this.photoUrl = photoUrl;
        this.tags = tags;
    }


}
