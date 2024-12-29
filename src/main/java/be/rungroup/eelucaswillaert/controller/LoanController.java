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
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static be.rungroup.eelucaswillaert.service.impl.ProductServiceImpl.mapToProductDto;

@Controller
@Service
@RequestMapping("/basket")
public class LoanController {

    @Autowired
    private LoanService loanService;
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String basketView(Model model) {
        /*User user = (User) session.getAttribute("loggedUser");
        if (user == null){
            model.addAttribute("error", "Je moet ingelogd zijn om je winkelmandje te bekijken.");
            return "redirect:/login";
        }

        LoanDTO loanDto = loanService.getLoanByUserId(user.getId());
        model.addAttribute("basket", loanDto);
        return "/basket/basket-view";
    */

        // Simuleer een LoanDTO voor testdoeleinden
        LoanDTO loanDto = new LoanDTO();
        loanDto.setId(1L);
        loanDto.setUserId(1L); // Mock user ID
        loanDto.setProducts(List.of(
                new Product(1L, "Product A", "Description A", 10, "urlA", List.of()),
                new Product(2L, "Product B", "Description B", 5, "urlB", List.of())
        ));
        loanDto.setQuantity(2);
        loanDto.setStartDate(LocalDateTime.now());
        loanDto.setEndDate(LocalDateTime.now().plusDays(7));

        // Voeg de mock basket toe aan het model
        model.addAttribute("loan", loanDto);
        return "/basket/basket-view"; // Thymeleaf-template
    }


    @PostMapping("/basket/add")
    public String addToBasket(
            @RequestParam Long productId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            model.addAttribute("error", "You must be logged in to add products to your basket.");
            return "redirect:/login";
        }

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
