package be.rungroup.eelucaswillaert.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class LoanDTO {
    private Long userId;
    private Long productId;
    private int quantity;
    private LocalDateTime endDate;
}
