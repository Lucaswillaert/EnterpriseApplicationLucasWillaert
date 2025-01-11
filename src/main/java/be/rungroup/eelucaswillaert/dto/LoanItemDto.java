package be.rungroup.eelucaswillaert.dto;


import be.rungroup.eelucaswillaert.model.Loan;
import be.rungroup.eelucaswillaert.model.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanItemDto {
    private Long id;
    private Loan loan;
    private Product product;
    private int quantity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean returned;

}
