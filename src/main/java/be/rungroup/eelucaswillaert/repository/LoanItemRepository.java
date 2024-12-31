package be.rungroup.eelucaswillaert.repository;

import be.rungroup.eelucaswillaert.model.LoanItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LoanItemRepository extends JpaRepository<LoanItem, Long> {

    @Query("SELECT li FROM LoanItem li WHERE li.loan.loan_id = :loanId")
    List<LoanItem> findByLoanId(@Param("loanId") Long loan_id);
}
