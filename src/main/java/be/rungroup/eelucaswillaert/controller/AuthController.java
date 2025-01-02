package be.rungroup.eelucaswillaert.controller;


import be.rungroup.eelucaswillaert.dto.LoginDto;
import be.rungroup.eelucaswillaert.dto.RegistrationDto;
import be.rungroup.eelucaswillaert.model.User;
import be.rungroup.eelucaswillaert.repository.UserRepository;
import be.rungroup.eelucaswillaert.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UserRepository userRepository;
    private UserService userService;

    @Autowired
    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpServletRequest request){
        model.addAttribute("currentUri", request.getRequestURI());
        return "/auth/login";
    }

    @PostMapping("/login")
    public String loginPagePost(Model model , HttpSession session, LoginDto loginDto){
        User user = userRepository.findByEmail(loginDto.getEmail());

        if (user != null && user.getPassword().equals(loginDto.getPassword())) {
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
    public String registerUser(RegistrationDto registrationDto) {
        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPassword(registrationDto.getPassword());
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, HttpSession session, HttpServletRequest request) {
        User user = (User) session.getAttribute("loggedUser");
        model.addAttribute("user", user);
        model.addAttribute("currentUri", request.getRequestURI());
        return "/user/user-profile";
    }





}
