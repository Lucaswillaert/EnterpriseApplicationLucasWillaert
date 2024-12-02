package be.rungroup.eelucaswillaert.repository;

import be.rungroup.eelucaswillaert.model.Loan;
import be.rungroup.eelucaswillaert.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface LoanRepository extends JpaRepository<Loan, Long> {


    // Vind alle loans van een specifieke gebruiker die nog niet afgerond zijn
    Optional<Loan> findByUserAndReturnedFalse(User user);


}
