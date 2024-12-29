package be.rungroup.eelucaswillaert.dto;

import be.rungroup.eelucaswillaert.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDTO {
    private Long id;
    private Long userId;
    private List<Product> products; // Lijst van product-IDs
    private int quantity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

