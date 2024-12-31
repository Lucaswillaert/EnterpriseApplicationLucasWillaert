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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static be.rungroup.eelucaswillaert.service.impl.ProductServiceImpl.mapToProductDto;

@Controller
@Service
@RequestMapping("/basket")
public class LoanController {

    private static final Logger logger = LoggerFactory.getLogger(LoanController.class); // for logging

    @Autowired
    private LoanService loanService;
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    @GetMapping
    public String basketView(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login"; // Of maak een mock-gebruiker
        }

        /*if (loan == null) {
            loan = new Loan();
            loan.setProducts(new ArrayList<>());
            session.setAttribute("basket", loan);
        }*/

        Loan loan = loanRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Loan newLoan = new Loan(user, new ArrayList<>(), 0, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
                    loanRepository.save(newLoan);
                    return newLoan;
                });

        model.addAttribute("loan", loan);
        return "basket/basket-view"; // Thymeleaf-template
    }

    @PostMapping("/add")
    public String addToBasket(
            @RequestParam Long productId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            HttpSession session,
            Model model
    ) {
        /*User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            model.addAttribute("error", "Je moet ingelogd zijn om producten toe te voegen aan je winkelmandje.");
            return "redirect:/login";
        }*/
         //Simuleer een ingelogde gebruiker als er geen gebruiker in de sessie zit
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            // Mock-gebruiker aanmaken
            user = new User();
            user.setId(1L); // Gebruik een fictief ID
            user.setVoornaam("John");
            user.setAchternaam("Doe");
            user.setEmail("john.Doe@gmail.com");
            user.setPassword("password");
            user.setAdmin(false);
            session.setAttribute("loggedUser", user); // Voeg mock-gebruiker toe aan de sessie
        } try {
            // Datum validatie
            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDate).atStartOfDay();
            if (end.isBefore(start)) {
                model.addAttribute("error", "De einddatum moet na de startdatum liggen.");
                model.addAttribute("product", productRepository.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Product niet gevonden")));
                return "products/product-detail";
            }
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product niet gevonden"));
            // Haal of maak het winkelmandje in de sessie
            Loan loan = (Loan) session.getAttribute("basket");
            if (loan == null) {
                loan = new Loan(user, new ArrayList<>(), 0, start, end);
                session.setAttribute("basket", loan);
            }
            loanService.addProductToBasket(user, product, start, end);

            return "redirect:/basket";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Er is een fout opgetreden: " + e.getMessage());
            System.out.println("------------Er is een fout opgetreden: " + e.getMessage() + "------------");
            return "products/product-detail";
        }
    }

    @PostMapping("/remove")
    public String removeFromBasket(@RequestParam Long productId, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");

            if (user == null) {
                // Mock user creation
                user = new User();
                user.setId(1L); // Use a fictitious ID
                user.setVoornaam("John");
                user.setAchternaam("Doe");
                user.setEmail("john.Doe@gmail.com");
                user.setPassword("password");
                user.setAdmin(false);
                session.setAttribute("loggedUser", user); // Add mock user to session
            }

        Loan loan = loanRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Loan not found for user"));

        loan.getProducts().removeIf(product -> product.getId().equals(productId));
        loan.setQuantity(loan.getQuantity() - 1);
        loanRepository.save(loan);

        return "redirect:/basket";
    }





}
