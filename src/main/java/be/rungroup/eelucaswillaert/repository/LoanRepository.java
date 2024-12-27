package be.rungroup.eelucaswillaert.repository;

import be.rungroup.eelucaswillaert.model.Loan;
import be.rungroup.eelucaswillaert.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findByUserId(Long userId);


}
