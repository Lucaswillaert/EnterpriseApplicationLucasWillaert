package be.rungroup.eelucaswillaert.service;

import be.rungroup.eelucaswillaert.dto.LoanDTO;

public interface LoanService {

     void borrowProduct(LoanDTO loanDTO);

     void returnProduct(Long loanId);



}
