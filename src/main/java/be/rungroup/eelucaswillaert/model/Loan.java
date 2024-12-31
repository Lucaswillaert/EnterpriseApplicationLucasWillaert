package be.rungroup.eelucaswillaert.model;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Loan implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "loan_products",
            joinColumns = @JoinColumn(name = "loan_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();
    private LocalDateTime startDate;
    private LocalDateTime endDate;


    private int quantity;


    public Loan (User user, List<Product> products, int quantity, LocalDateTime startDate, LocalDateTime endDate) {
        this.user = user;
        this.products = products;
        this.quantity = quantity;
        this.startDate = startDate;
        this.endDate = endDate;

    }




}
