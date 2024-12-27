package be.rungroup.eelucaswillaert.dto;

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
    private List<Long> productIds; // Lijst van product-IDs
    private int quantity;
}

