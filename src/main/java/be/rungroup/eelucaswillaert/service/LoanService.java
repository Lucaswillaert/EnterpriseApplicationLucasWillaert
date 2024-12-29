package be.rungroup.eelucaswillaert.service;

import be.rungroup.eelucaswillaert.dto.LoanDTO;
import be.rungroup.eelucaswillaert.model.Product;
import be.rungroup.eelucaswillaert.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface LoanService {

     void addProductToBasket(User user , Product product, LocalDateTime startDate, LocalDateTime endDate);

     void returnProduct(Long loanId, Long ProductId);

     int findQuantityPerProduct(Long loanId, Long productId);

     LoanDTO getLoanByUserId(Long id);
}
