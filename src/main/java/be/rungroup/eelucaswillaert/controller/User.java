package be.rungroup.eelucaswillaert.controller;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
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
