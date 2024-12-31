package be.rungroup.eelucaswillaert.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Serializable {
    @Transient
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String voornaam;
    private String achternaam;
    private String email;
    private String password;
    private boolean isAdmin;

    //@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    //    private List<Loan> loans = new ArrayList<>();


}
