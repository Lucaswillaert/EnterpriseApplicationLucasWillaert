package be.rungroup.eelucaswillaert.controller;
import be.rungroup.eelucaswillaert.dto.LoanDTO;
import be.rungroup.eelucaswillaert.model.Loan;
import be.rungroup.eelucaswillaert.model.LoanItem;
import be.rungroup.eelucaswillaert.model.Product;
import be.rungroup.eelucaswillaert.model.User;
import be.rungroup.eelucaswillaert.repository.LoanRepository;
import be.rungroup.eelucaswillaert.repository.ProductRepository;
import be.rungroup.eelucaswillaert.repository.UserRepository;
import be.rungroup.eelucaswillaert.service.LoanService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
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
    public String basketView(HttpSession session, Model model, HttpServletRequest request) {

        User user = (User) session.getAttribute("loggedUser");

        final User finalUser = user;
        Loan loan = loanRepository.findByUserId(finalUser.getId())
                .orElseGet(() -> loanRepository.save(new Loan(finalUser, new ArrayList<>())));

        LoanDTO loanDTO = loanService.getLoanByUserId(finalUser.getId());
        model.addAttribute("loan", loanDTO);
        model.addAttribute("currentUri", request.getRequestURI());
        return "basket/basket-view"; // Thymeleaf-template
    }


    //TODO fix de error dat als enddate voor startdate is dat er een error komt
    //dit ligt wss bij de end.isBefore of meegeven van data, check de erorrs nog eens.
    @PostMapping("/add")
    public String addToBasket(
            @RequestParam("product_id") Long product_id,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            HttpSession session,
            HttpServletRequest request,
            Model model
    ) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            model.addAttribute("error", "You must be logged in to add products to your basket.");
            return "redirect:/login";
        }
        try {
            // Date validation
            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
            logger.error("Start date: {}", start);
            LocalDateTime end = LocalDate.parse(endDate).atStartOfDay();
            logger.error("End date: {}", end);
            LocalDateTime now = LocalDateTime.now();
            if (end.isBefore(start)) {
                logger.error("End date cannot be before start date.");
                model.addAttribute("error", "End date cannot be before start date.");
                return "/products/product-detail";
            } else if (start.isBefore(now) || end.isBefore(now)) {
                model.addAttribute("error", "Dates cannot be in the past.");
                return "/products/product-detail";
            }

            // Add product to basket
            loanService.addProductToBasket(user.getId(), product_id, start, end);
            return "redirect:/basket";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred:---------- " + e.getMessage());
            return "/products/product-detail";
        }
    }

    @PostMapping("/remove")
    public String removeFromBasket(@RequestParam("product_id") Long product_id, HttpSession session, HttpServletRequest request, Model model) {
        User user = (User) session.getAttribute("loggedUser");
            try {
            Loan loan = loanRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Loan not found for user"));

            LoanItem loanItem = loan.getLoanItems().stream()
                    .filter(item -> item.getProduct().getProduct_id().equals(product_id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Product niet gevonden in basket"));

            loanService.returnProduct(loan.getLoan_id(), product_id);
            return "redirect:/basket";

        } catch (Exception e) {
            logger.error("Er is een fout opgetreden bij het verwijderen uit de basket: {}", e.getMessage());
            model.addAttribute("currentUri", request.getRequestURI());
            return "redirect:/basket?error=" + e.getMessage();
        }
    }

    @GetMapping("/checkout")
    public String checkoutPage(Model model, HttpSession session, HttpServletRequest request) {
        logger.info("GET /basket/checkout called"); // Voeg logging toe
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            logger.warn("User is not logged in");
            return "redirect:/login";
        }

        Loan loan = loanRepository.findByUserId(user.getId()).orElse(null);
        model.addAttribute("loan", loan);
        model.addAttribute("currentUri", request.getRequestURI());
        return "basket/checkout";
    }

    @PostMapping("/checkout/confirm")
    public String confirmCheckout(HttpSession session, RedirectAttributes redirectAttributes) {
        logger.info("POST /basket/checkout/confirm called");
        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Je moet ingelogd zijn om deze actie uit te voeren.");
            return "redirect:/login";
        }
        try {
            LoanDTO loanDTO = loanService.getLoanByUserId(user.getId());
            List<String> availabilityMessages = loanService.checkAvailability(loanDTO);

            if (!availabilityMessages.isEmpty()) {
                // Voeg specifieke melding met product en periode toe
                redirectAttributes.addFlashAttribute("availabilityMessages", availabilityMessages);
                redirectAttributes.addFlashAttribute("error", "Sommige items zijn niet beschikbaar. Pas je basket aan.");
                return "redirect:/basket/checkout";
            }
            // Als alles beschikbaar is, ga verder met de checkout
            String message = loanService.checkoutLoan(user.getId());
            redirectAttributes.addFlashAttribute("success", "LeenTicket geconfirmeerd! Bekijk 'Jouw profiel' voor een overzicht.");
            return "redirect:/profile";

        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/basket";
        }
    }




}
