package be.rungroup.eelucaswillaert.dto;

import be.rungroup.eelucaswillaert.model.LoanItem;
import be.rungroup.eelucaswillaert.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDTO {
    private Long loan_id;
    private Long userId;
    private List<LoanItemDto> loanItems = new ArrayList<>(); // Lijst van product-IDs
}

