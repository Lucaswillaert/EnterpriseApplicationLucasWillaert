package be.rungroup.eelucaswillaert.service.impl;

import be.rungroup.eelucaswillaert.dto.LoanDTO;
import be.rungroup.eelucaswillaert.model.Loan;
import be.rungroup.eelucaswillaert.model.Product;
import be.rungroup.eelucaswillaert.model.User;
import be.rungroup.eelucaswillaert.repository.LoanRepository;
import be.rungroup.eelucaswillaert.repository.ProductRepository;
import be.rungroup.eelucaswillaert.repository.UserRepository;
import be.rungroup.eelucaswillaert.service.LoanService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanrepository;
    private final ProductRepository productrepository;
    private final UserRepository userrepository;

    public LoanServiceImpl(LoanRepository loanrepository,
                           ProductRepository productrepository,
                           UserRepository userrepository
    ) {
        this.loanrepository = loanrepository;
        this.productrepository = productrepository;
        this.userrepository = userrepository;
    }


    public void addProductToBasket(User user,
                                   Product product,
                                   LocalDateTime startDate,
                                   LocalDateTime endDate
    ) {
        Loan loan = loanrepository.findByUserId(user.getId())
                .orElseGet(() -> loanrepository.save(
                        new Loan(user, new ArrayList<>(), 0, startDate, endDate)
                ));

        loan.getProducts().add(product);
        loan.setQuantity(loan.getQuantity() + 1);
        loan.setStartDate(startDate);
        loan.setEndDate(endDate);
        loanrepository.save(loan);
    }



    @Scheduled(cron = "0 0 0 * * ?") // Dagelijks om middernacht
    public void checkExpiredLoans() {
        List<Loan> expiredLoans = loanrepository.findAll().stream()
                .filter(loan -> loan.getEndDate().isBefore(LocalDateTime.now()))
                .toList();
        // Markeer of verwerk verlopen leningen
    }



    public LoanDTO getLoanByUserId(Long userId) {
        Loan loan = loanrepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found for user"));
        return mapToDTO(loan);
    }

    @Override
    public void returnProduct(Long loanId, Long productId) {
        Loan loan = loanrepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Not found!"));


        Product product = loan.getProducts().stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found in basket"));

        product.setTotalStock(product.getTotalStock() + 1);
        loan.getProducts().remove(product);
        loanrepository.save(loan);
        productrepository.save(product);
    }

    @Override
    public int findQuantityPerProduct(Long loanId, Long productId) {
        Optional<Loan> loanOptional = loanrepository.findById(loanId);
        if(loanOptional.isPresent()){
            Loan loan = loanOptional.get();
            return (int) loan.getProducts().stream()
                    .filter(product -> product.getId().equals(productId)) // Filter de producten op basis van de ID
                    .count(); // Tel het aantal producten

        }
        return 0;
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
                loan.getProducts(),
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
        loan.setProducts(loanDTO.getProducts());
        loan.setQuantity(loanDTO.getQuantity());
        loan.setStartDate(loanDTO.getStartDate());
        loan.setEndDate(loanDTO.getEndDate());
        return loan;
    }

    }


