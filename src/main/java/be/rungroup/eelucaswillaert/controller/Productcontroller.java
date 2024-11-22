package be.rungroup.eelucaswillaert.controller;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Service
public class Productcontroller {

    @GetMapping("/products")
    public String showAllProducts() {
        return "product-list";
    }



}
