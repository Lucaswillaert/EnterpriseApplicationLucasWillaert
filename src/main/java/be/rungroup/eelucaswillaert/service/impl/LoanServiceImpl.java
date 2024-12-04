package be.rungroup.eelucaswillaert.service.impl;

import be.rungroup.eelucaswillaert.dto.LoanDTO;
import be.rungroup.eelucaswillaert.dto.ProductDto;
import be.rungroup.eelucaswillaert.model.Loan;
import be.rungroup.eelucaswillaert.model.Product;
import be.rungroup.eelucaswillaert.model.User;
import be.rungroup.eelucaswillaert.repository.LoanRepository;
import be.rungroup.eelucaswillaert.repository.ProductRepository;
import be.rungroup.eelucaswillaert.repository.UserRepository;
import be.rungroup.eelucaswillaert.service.LoanService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanrepository;
    private final ProductRepository productrepository;
    private final UserRepository userrepository;

    public LoanServiceImpl(LoanRepository loanrepository, ProductRepository productrepository, UserRepository userrepository) {
        this.loanrepository = loanrepository;
        this.productrepository = productrepository;
        this.userrepository = userrepository;
    }


    public void addProductToLoan(User user, ProductDto productDTO) {

    }

    public LoanDTO getLoanById(Long id) {
        Loan loan = loanrepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        return mapToDTO(loan);
    }

    @Override
    public void returnProduct(Long loanId) {
        Loan loan = loanrepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found!"));

        // Verhoog de voorraad van elk product in de loan
        for (Product product : loan.getProducts()) {
            product.setTotalStock(product.getTotalStock() + 1); // Of loan.getQuantity() als dat per product geldt
            productrepository.save(product);
        }

        // Verwijder de Loan
        loanrepository.delete(loan);
    }


    public void deleteLoan(Long id) {
        Loan loan = loanrepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        // Verhoog de voorraad van elk product in de loan
        for (Product product : loan.getProducts()) {
            product.setTotalStock(product.getTotalStock() + 1); // Of loan.getQuantity() als dat per product geldt
            productrepository.save(product);
        }

        // Verwijder de Loan
        loanrepository.deleteById(id);
    }



    private LoanDTO mapToDTO(Loan loan) {
        return new LoanDTO(
                loan.getId(),
                loan.getUser().getId(),
                loan.getProducts().stream() // Gebruik de collectie van producten
                        .map(Product::getId)    // Haal de IDs van de producten op
                        .collect(Collectors.toList()), // Verzamel in een lijst
                loan.getQuantity(),
                loan.getStartDate(),
                loan.getEndDate()
        );
    }

    private Loan mapToLoan(LoanDTO loanDTO) {
        Loan loan = new Loan();
        loan.setId(loanDTO.getId());
        loan.setUser(userrepository.findById(loanDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
        loan.setProducts(loanDTO.getProductIds().stream()
                .map(productId -> productrepository.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Product not found")))
                .collect(Collectors.toList()));
        loan.setQuantity(loanDTO.getQuantity());
        loan.setStartDate(loanDTO.getStartDate());
        loan.setEndDate(loanDTO.getEndDate());
        return loan;
    }

    }


