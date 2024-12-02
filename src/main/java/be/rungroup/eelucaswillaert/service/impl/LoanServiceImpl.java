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

import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
        // Map de ProductDTO naar een Product entity
        Product product = productrepository.findById(productDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Product niet gevonden met ID: " + productDTO.getId()));

        // Controleer of er voldoende voorraad is
        if (product.getTotalStock() <= 0) {
            throw new IllegalStateException("Het product is niet meer beschikbaar.");
        }

        // Zoek een actieve loan voor de gebruiker
        Loan loan = loanrepository.findByUserAndReturnedFalse(user).orElseGet(() -> {
            Loan newLoan = new Loan();
            newLoan.setUser(user);
            newLoan.setStartDate(LocalDateTime.now());
            newLoan.setEndDate(LocalDateTime.now().plusDays(7)); // Stel een standaard uitleentermijn in
            return loanrepository.save(newLoan);
        });

        // Controleer of het product al in de loan zit
        if (loan.getProducts().contains(product)) {
            throw new IllegalStateException("Het product is al toegevoegd aan de loan.");
        }

        // Voeg het product toe aan de loan
        loan.getProducts().add(product);

        // Verminder de hoeveelheid van het product
        product.setTotalStock(product.getTotalStock() - productDTO.getTotalStock());
        productrepository.save(product);


        // Sla de loan op
        loanrepository.save(loan);
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


