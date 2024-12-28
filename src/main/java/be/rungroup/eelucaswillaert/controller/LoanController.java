package be.rungroup.eelucaswillaert.controller;

import be.rungroup.eelucaswillaert.dto.LoanDTO;
import be.rungroup.eelucaswillaert.dto.ProductDto;
import be.rungroup.eelucaswillaert.model.Loan;
import be.rungroup.eelucaswillaert.model.Product;
import be.rungroup.eelucaswillaert.model.User;
import be.rungroup.eelucaswillaert.repository.LoanRepository;
import be.rungroup.eelucaswillaert.repository.ProductRepository;
import be.rungroup.eelucaswillaert.repository.UserRepository;
import be.rungroup.eelucaswillaert.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static be.rungroup.eelucaswillaert.service.impl.ProductServiceImpl.mapToProductDto;

@Controller
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/basket")
    public ResponseEntity<LoanDTO> basketView(@RequestParam Long userId) {
        try {
            // Haal de loan op van de gebruiker
            Loan loan = loanRepository.findByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("No basket found for the user"));

            // Map de loan naar een LoanDTO (of een aangepaste response)
            LoanDTO loanDTO = loanService.getLoanById(loan.getId());

            return ResponseEntity.ok(loanDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    //TODO: Implementeer deze methode:borrowProduct
    @PostMapping("/basket/add")
    public String addToBasket(
            @RequestParam Long productId,
            @RequestParam Long userId, // Dit is de ID van de ingelogde gebruiker
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Principal principal,
            Model model
    ) {
        try {
            // Validatie van de datums
            if (startDate == null || endDate == null || startDate.isEmpty() || endDate.isEmpty()) {
                model.addAttribute("error", "Geef een datum in wanneer je dit wilt uitlenen.");
                model.addAttribute("product", productRepository.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Product not found")));
                return "products/product-detail"; // Verwijst naar je productdetails-pagina
            }

            // Parse de datums
            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDate).atStartOfDay();

            // Controleer of de einddatum na de startdatum ligt
            if (end.isBefore(start)) {
                model.addAttribute("error", "De einddatum moet na de startdatum liggen.");
                model.addAttribute("product", productRepository.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Product not found")));
                return "products/product-detail"; // Verwijst naar je productdetails-pagina
            }

            // Voeg het product toe aan het winkelmandje
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            loanService.addProductToBasket(user, product, start, end);
            return "redirect:/basket/view";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "products/product-detail"; // Verwijst naar je productdetails-pagina
        }
    }




}
