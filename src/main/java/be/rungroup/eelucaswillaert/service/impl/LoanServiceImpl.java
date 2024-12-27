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
import java.util.ArrayList;
import java.util.Optional;
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

        // Zoek naar een loan van de gebruiker
        Loan loan = loanrepository.findByUserId(user.getId())
                .orElse(new Loan(user,new ArrayList<>(),0));

        Product product = productrepository.findById(productDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        loan.getProducts().add(product);
        loan.setQuantity(loan.getQuantity() + 1);

        loanrepository.save(loan);
    }

    public LoanDTO getLoanById(Long id) {
        Loan loan = loanrepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        return mapToDTO(loan);
    }

    @Override
    public void returnProduct(Long loanId, Long productId) {
        Loan loan = loanrepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found!"));

        //Vind het product in de loan
       Optional <Product> productOptional = loan.getProducts().stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst();

       if(productOptional.isPresent()){
           Product product = productOptional.get();
           product.setTotalStock(product.getTotalStock() + 1);
           productrepository.save(product);

           //verwijder de product van de loan als de quantity 0 is
           loan.getProducts().remove(product);
           loanrepository.save(loan);
       } else {
           throw new IllegalArgumentException("Product not found in loan");
       }
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
                loan.getProducts().stream() // Gebruik de collectie van producten
                        .map(Product::getId)    // Haal de IDs van de producten op
                        .collect(Collectors.toList()), // Verzamel in een lijst
                loan.getQuantity()
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
        return loan;
    }

    }


