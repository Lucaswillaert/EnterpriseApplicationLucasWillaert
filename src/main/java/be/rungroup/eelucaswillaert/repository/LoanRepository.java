package be.rungroup.eelucaswillaert.repository;

import be.rungroup.eelucaswillaert.model.Loan;
import be.rungroup.eelucaswillaert.model.LoanItem;
import be.rungroup.eelucaswillaert.model.Product;
import be.rungroup.eelucaswillaert.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findByUserId(Long userId);

    @Query("SELECT li FROM LoanItem li WHERE li.product IN :products " +
            "AND ((li.startDate < :endDate AND li.endDate > :startDate)) " +
            "AND li.product.totalStock = 0")
    List<LoanItem> findAllByProductInAndDateRange(@Param("products") List<Product> products,
                                                  @Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);

    //voor de user om zijn loans te zien
    @Query("SELECT l FROM Loan l WHERE l.user.id = :userId")
    List<Loan> findAllByUserId(@Param("userId") Long userId);

    //voor de admin om alle loans te zien
    @Query("SELECT l FROM Loan l")
    List<Loan> findAllLoans();


    //JPA query om alle users met actieve loans te vinden
    @Query("SELECT DISTINCT l.user FROM Loan l WHERE l.loanItems IS NOT EMPTY")
    List<User> findUsersWithActiveLoans();

}
