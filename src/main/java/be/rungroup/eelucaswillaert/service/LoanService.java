package be.rungroup.eelucaswillaert.service;

import be.rungroup.eelucaswillaert.dto.LoanDTO;
import be.rungroup.eelucaswillaert.model.Loan;
import be.rungroup.eelucaswillaert.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("loanService")
public interface LoanService {

     void addProductToBasket(Long userId, Long productId, LocalDateTime startDate, LocalDateTime endDate);

     void returnProduct(Long loanId, Long productId);

     LoanDTO getLoanByUserId(Long userId);

     void deleteLoan(Long loanId);

     String checkoutLoan(Long userId);

     List<String> checkAvailability(LoanDTO loanDTO);

     boolean isProductAvailable(Long productId, LocalDateTime startDate, LocalDateTime endDate);

     List<Loan> getLoansForUser(Long userId); // Alleen voor student
     List<Loan> getAllLoans(); // Alleen voor admin

     List<User> getUsersWithActiveLoans();

}
