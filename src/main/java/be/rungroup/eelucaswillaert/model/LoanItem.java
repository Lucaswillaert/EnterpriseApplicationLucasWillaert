package be.rungroup.eelucaswillaert.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LoanItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_id" , referencedColumnName = "loan_id", nullable = false)
    @ToString.Exclude
    private Loan loan;

    @ManyToOne
    @JoinColumn(name = "product_id" , nullable = false)
    private Product product;

    private int quantity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private boolean returned;  // This will track whether the item has been returned


    public LoanItem(Product product, int quantity, LocalDateTime startDate, LocalDateTime endDate, boolean returned) {
        this.product = product;
        this.quantity = quantity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.returned = returned;
    }


}

