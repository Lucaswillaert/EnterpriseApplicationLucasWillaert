package be.rungroup.eelucaswillaert.model;


import jakarta.persistence.*;

import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
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
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    private String name;

    private String description;

    private int totalStock;

    @Lob
    private byte[] photo;


    @ElementCollection
    @CollectionTable(
            name = "product_tags",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    )
    @Enumerated(EnumType.STRING)
    private List<Tag> tags = new ArrayList<>();


}
