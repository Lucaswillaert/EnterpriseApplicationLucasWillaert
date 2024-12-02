package be.rungroup.eelucaswillaert.service;

import be.rungroup.eelucaswillaert.dto.ProductDto;
import be.rungroup.eelucaswillaert.model.User;
import org.springframework.stereotype.Service;

@Service
public interface LoanService {

     void addProductToLoan(User user , ProductDto productDto);

     void returnProduct(Long loanId);



}
