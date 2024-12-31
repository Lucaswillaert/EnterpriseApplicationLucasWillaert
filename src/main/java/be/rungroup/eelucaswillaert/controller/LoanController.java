package be.rungroup.eelucaswillaert.controller;

import be.rungroup.eelucaswillaert.dto.LoanDTO;
import be.rungroup.eelucaswillaert.dto.LoanItemDto;
import be.rungroup.eelucaswillaert.dto.ProductDto;
import be.rungroup.eelucaswillaert.model.Loan;
import be.rungroup.eelucaswillaert.model.LoanItem;
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
    @Autowired
    private Product product;


    @GetMapping
    public String basketView(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            // Mock-gebruiker aanmaken
            final User mockUser = new User();
            mockUser.setId(1L); // Gebruik een fictief ID
            mockUser.setVoornaam("John");
            mockUser.setAchternaam("Doe");
            mockUser.setEmail("john.Doe@gmail.com");
            mockUser.setPassword("password");
            mockUser.setAdmin(false);
            user = mockUser;
        // User user = (User) session.getAttribute("loggedUser");
        //if (user == null) {
            //return "redirect:/login"; // Redirect naar login indien geen gebruiker ingelogd
        }
        final User finalUser = user;
        Loan loan = loanRepository.findByUserId(finalUser.getId())
                .orElseGet(() -> loanRepository.save(new Loan(finalUser, new ArrayList<>())));

        LoanDTO loanDTO = loanService.getLoanByUserId(finalUser.getId());
        model.addAttribute("loan", loanDTO);

        return "basket/basket-view"; // Thymeleaf-template
    }

    @PostMapping("/add")
    public String addToBasket(
            @RequestParam("product_id") Long product_id,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
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
            logger.error("we zitten nu voor de local datum validatie ");
            // Datum validatie
            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDate).atStartOfDay();
            LocalDateTime now = LocalDateTime.now();
            if (end.isBefore(start)) {
                model.addAttribute("error", "De einddatum moet na de startdatum liggen.");
                model.addAttribute("product", productRepository.findById(product_id)
                        .orElseThrow(() -> new IllegalArgumentException("Product niet gevonden")));
                return "products/product-detail";
            } else if (start.isBefore(now) || end.isBefore(now)) {
                model.addAttribute("error", "De startdatum en einddatum mogen niet in het verleden liggen.");
                model.addAttribute("product", productRepository.findById(product_id)
                        .orElseThrow(() -> new IllegalArgumentException("Product niet gevonden")));
                return "products/product-detail";
            }

            logger.error("Product toevoegen aan winkelmandje: product_id={}, start={}, end={}", product_id, start, end);
            loanService.addProductToBasket(user.getId(), product_id, start, end);

            logger.error("Product toegevoegd aan winkelmandje: product_id={},productName={}, start={}, end={}", product_id,product.getName(), start, end);
            return "redirect:/basket";

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Er is een fout opgetreden: " + e.getMessage());
            model.addAttribute("error", "Er is een fout opgetreden: " + e.getMessage());
            logger.error("Er is een fout opgetreden bij het toevoegen aan de basket: {}", e.getMessage());
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
            try {
            Loan loan = loanRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Loan not found for user"));

            LoanItem loanItem = loan.getLoanItems().stream()
                    .filter(item -> item.getProduct().getProduct_id().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Product niet gevonden in basket"));

            loanService.returnProduct(loan.getLoan_id(), productId);
            return "redirect:/basket";

        } catch (Exception e) {
            logger.error("Er is een fout opgetreden bij het verwijderen uit de basket: {}", e.getMessage());
            return "redirect:/basket?error=" + e.getMessage();
        }
    }





}
