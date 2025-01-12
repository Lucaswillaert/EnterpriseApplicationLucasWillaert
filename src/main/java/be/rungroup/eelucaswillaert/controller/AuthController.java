package be.rungroup.eelucaswillaert.controller;


import be.rungroup.eelucaswillaert.dto.LoanDTO;
import be.rungroup.eelucaswillaert.dto.LoginDto;
import be.rungroup.eelucaswillaert.dto.RegistrationDto;
import be.rungroup.eelucaswillaert.model.Loan;
import be.rungroup.eelucaswillaert.model.LoanItem;
import be.rungroup.eelucaswillaert.model.Product;
import be.rungroup.eelucaswillaert.model.User;
import be.rungroup.eelucaswillaert.repository.LoanItemRepository;
import be.rungroup.eelucaswillaert.repository.LoanRepository;
import be.rungroup.eelucaswillaert.repository.ProductRepository;
import be.rungroup.eelucaswillaert.repository.UserRepository;
import be.rungroup.eelucaswillaert.service.LoanService;
import be.rungroup.eelucaswillaert.service.UserService;
import be.rungroup.eelucaswillaert.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class AuthController {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final LoanRepository loanRepository;
    private final LoanService loanService;
    private final LoanItemRepository loanItemRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userServiceImpl;
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService,
                          UserRepository userRepository,
                          LoanRepository loanRepository,
                          LoanService loanService,
                          LoanItemRepository loanItemRepository,
                          ProductRepository productRepository, PasswordEncoder passwordEncoder, UserServiceImpl userServiceImpl) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.loanService = loanService;
        this.loanItemRepository = loanItemRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpServletRequest request){
        model.addAttribute("currentUri", request.getRequestURI());
        return "/auth/login";
    }

    //@Valid: Deze annotatie zorgt ervoor dat de validatie wordt uitgevoerd op het object dat als parameter wordt doorgegeven.
    //BindingResult: Dit object bevat de resultaten van de validatie en eventuele fouten die zijn opgetreden. Hiermee kun je controleren of er validatiefouten zijn en deze afhandelen.
    @PostMapping("/login")
    public String loginPagePost(Model model , HttpSession session, @Valid @ModelAttribute LoginDto loginDto,BindingResult result){
        if (result.hasErrors()) {
            model.addAttribute("loginDto", loginDto);
            return "/auth/login";
        }
        User user = userRepository.findByEmail(loginDto.getEmail());
        if (user != null && passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            session.setAttribute("loggedUser", user);
            return "redirect:/products/product-list";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "/auth/login";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate(); // invalidate the session to logout the user
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model, HttpServletRequest request) {
        model.addAttribute("registrationDto", new RegistrationDto());
        model.addAttribute("currentUri", request.getRequestURI());
        return "/auth/register";
    }

    @PostMapping("/register/save")
    public String registerUser(@Valid @ModelAttribute RegistrationDto registrationDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("registrationDto", registrationDto);
            return "/auth/register";
        }
        userServiceImpl.saveUser(registrationDto);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, HttpSession session, HttpServletRequest request) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        if (user.isAdmin()) {
            List<Loan> allLoans = loanService.getAllLoans();
            model.addAttribute("allLoans", allLoans);
        } else {
            List<Loan> userLoan = loanService.getLoansForUser(user.getId());
            System.out.println("Loans for user: " + userLoan);  // Debugging
            model.addAttribute("loans", userLoan);
        }
        model.addAttribute("user", user);
        model.addAttribute("currentUri", request.getRequestURI());
        return "/user/user-profile";
    }

    @PostMapping("/admin/return")
    public String markItemsReturned(@RequestParam List<Long> returnedItems) {
        for (Long loanItemId : returnedItems) {
            Optional<LoanItem> optionalLoanItem = loanItemRepository.findLoanItemById(loanItemId);

            if (optionalLoanItem.isPresent()) {
                LoanItem loanItem = optionalLoanItem.get();

                // Controleer of het item nog niet is geretourneerd
                if (!loanItem.isReturned()) {
                    loanItem.setReturned(true);
                    loanItemRepository.save(loanItem);

                    // Update de voorraad van het bijbehorende product
                    Product product = loanItem.getProduct();
                    product.setTotalStock(product.getTotalStock() + 1);
                    productRepository.save(product);
                    System.out.println("Product stock updated" + product.getTotalStock() + " " + product.getName());
                }
            }
        }

        return "redirect:/user-profile";
    }


}
