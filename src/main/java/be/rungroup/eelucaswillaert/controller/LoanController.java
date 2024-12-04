package be.rungroup.eelucaswillaert.controller;

import be.rungroup.eelucaswillaert.dto.LoanDTO;
import be.rungroup.eelucaswillaert.dto.ProductDto;
import be.rungroup.eelucaswillaert.model.Product;
import be.rungroup.eelucaswillaert.model.User;
import be.rungroup.eelucaswillaert.repository.ProductRepository;
import be.rungroup.eelucaswillaert.repository.UserRepository;
import be.rungroup.eelucaswillaert.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    //TODO: Implementeer deze methode:borrowProduct
    @PostMapping("/borrow")
    public ResponseEntity<String> borrowProduct(@RequestBody LoanDTO loanDTO) {
        try {
            // Haal de gebruiker op uit de UserRepository
            User user = userRepository.findById(loanDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Haal het product op uit de ProductRepository
            Product product = productRepository.findById(loanDTO.getProductIds().get(0)) // Eerste product-ID nemen
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            // Voeg het product toe aan de Loan via de service
            //loanService.addProductToLoan(user, product);

            return ResponseEntity.ok("Product successfully borrowed!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/return/{loanId}")
    public ResponseEntity<String> returnProduct(@PathVariable Long loanId) {
        loanService.returnProduct(loanId);
        return ResponseEntity.ok("Product successfully returned!");
    }
}
