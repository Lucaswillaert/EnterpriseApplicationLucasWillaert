package be.rungroup.eelucaswillaert.service.impl;

import be.rungroup.eelucaswillaert.dto.LoanDTO;
import be.rungroup.eelucaswillaert.dto.LoanItemDto;
import be.rungroup.eelucaswillaert.model.Loan;
import be.rungroup.eelucaswillaert.model.LoanItem;
import be.rungroup.eelucaswillaert.model.Product;
import be.rungroup.eelucaswillaert.model.User;
import be.rungroup.eelucaswillaert.repository.LoanRepository;
import be.rungroup.eelucaswillaert.repository.ProductRepository;
import be.rungroup.eelucaswillaert.repository.UserRepository;
import be.rungroup.eelucaswillaert.service.LoanService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public LoanServiceImpl(LoanRepository loanRepository,
                           ProductRepository productRepository,
                           UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public void addProductToBasket(Long userId,
                                   Long productId,
                                   LocalDateTime startDate,
                                   LocalDateTime endDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        Loan loan = loanRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Loan newLoan = new Loan(user, new ArrayList<>());
                    return loanRepository.saveAndFlush(newLoan); // Force immediate database save

                });


        if (loan.getLoan_id() == null) {
            throw new IllegalStateException("Loan ID should not be null after saving.");
        }
        // Controleer of het product al in de basket zit
        Optional<LoanItem> existingLoanItem = loan.getLoanItems().stream()
                .filter(item -> item.getProduct().getProduct_id().equals(productId))
                .findFirst();

        System.out.println("Existing loan item: " + existingLoanItem);
        if (existingLoanItem.isPresent()) {
            LoanItem loanItem = existingLoanItem.get();
            loanItem.setQuantity(loanItem.getQuantity() + 1);
        } else {
            LoanItem loanItem = new LoanItem(product, 1, startDate, endDate);
            loanItem.setLoan(loan); // Ensure the association is explicitly set
            loan.getLoanItems().add(loanItem);
        }
        // Update voorraad en sla loan op
        product.setTotalStock(product.getTotalStock() - 1);
        loanRepository.save(loan);
        productRepository.save(product);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Dagelijks om middernacht
    public void checkExpiredLoans() {
        List<Loan> expiredLoans = loanRepository.findAll().stream()
                .filter(loan -> loan.getLoanItems().stream()
                        .anyMatch(item -> item.getEndDate() != null && item.getEndDate().isBefore(LocalDateTime.now())))
                .toList();

        for (Loan loan : expiredLoans) {
            System.out.println("Expired loan detected: " + loan.getLoan_id());
        }
    }

    public LoanDTO getLoanByUserId(Long userId) {
        Loan loan = loanRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found for user"));

        return mapToDTO(loan);
    }

    @Override
    public void returnProduct(Long loanId, Long productId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        LoanItem loanItem = loan.getLoanItems().stream()
                .filter(item -> item.getProduct().getProduct_id().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found in loan"));

        loanItem.setQuantity(loanItem.getQuantity() - 1);
        if (loanItem.getQuantity() <= 0) {
            loan.getLoanItems().remove(loanItem);
        }

        Product product = loanItem.getProduct();
        product.setTotalStock(product.getTotalStock() + 1);

        loanRepository.save(loan);
        productRepository.save(product);
    }

    @Override
    public int findQuantityPerProduct(Long loanId, Long productId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        return loan.getLoanItems().stream()
                .filter(item -> item.getProduct().getProduct_id().equals(productId))
                .mapToInt(LoanItem::getQuantity)
                .sum();
    }

    public void deleteLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        for (LoanItem item : loan.getLoanItems()) {
            Product product = item.getProduct();
            product.setTotalStock(product.getTotalStock() + item.getQuantity());
            productRepository.save(product);
        }

        loanRepository.deleteById(id);
    }

    private LoanDTO mapToDTO(Loan loan) {
        List<LoanItemDto> loanItemDTOs = loan.getLoanItems().stream()
                .map(item -> new LoanItemDto(
                        item.getId(),
                        loan, // Referentie naar de Loan
                        item.getProduct(),
                        item.getQuantity(),
                        item.getStartDate(),
                        item.getEndDate()))
                .toList();

        return new LoanDTO(
                loan.getLoan_id(),
                loan.getUser().getId(),
                loanItemDTOs
        );
    }


    private Loan mapToLoan(LoanDTO loanDTO) {
        Loan loan = new Loan();
        loan.setLoan_id(loanDTO.getLoan_id());
        loan.setUser(userRepository.findById(loanDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found")));

        List<LoanItem> loanItems = loanDTO.getLoanItems().stream()
                .map(dto -> {
                    Product product = productRepository.findById(dto.getProduct().getProduct_id())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
                    LoanItem loanItem = new LoanItem();
                    loanItem.setId(dto.getId());
                    loanItem.setProduct(product);
                    loanItem.setQuantity(dto.getQuantity());
                    loanItem.setStartDate(dto.getStartDate());
                    loanItem.setEndDate(dto.getEndDate());
                    return loanItem;
                })
                .toList();

        return loan;
    }

}
