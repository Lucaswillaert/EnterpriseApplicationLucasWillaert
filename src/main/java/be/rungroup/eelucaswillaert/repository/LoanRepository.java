package be.rungroup.eelucaswillaert.repository;

import be.rungroup.eelucaswillaert.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface LoanRepository extends JpaRepository<Loan, Long> {


}
