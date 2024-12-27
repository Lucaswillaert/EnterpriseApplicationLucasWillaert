package be.rungroup.eelucaswillaert.service;

import be.rungroup.eelucaswillaert.dto.ProductDto;
import be.rungroup.eelucaswillaert.model.Loan;
import be.rungroup.eelucaswillaert.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface LoanService {

     void addProductToLoan(User user , ProductDto productDto);

     void returnProduct(Long loanId, Long ProductId);

     int findQuantityPerProduct(Long loanId, Long productId);

}
