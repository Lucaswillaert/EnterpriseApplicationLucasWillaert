package be.rungroup.eelucaswillaert.controller;

import be.rungroup.eelucaswillaert.dto.ProductDto;
import be.rungroup.eelucaswillaert.model.Product;
import be.rungroup.eelucaswillaert.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Service
public class Productcontroller {

    @Autowired
    private ProductService productService;

    public Productcontroller(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String showAllProducts(Model model) {
        List<Product> products= productService.findAllProducts();
        model.addAttribute("products",products);

        return "product-list";
    }
    //TODO: add ALL API endpoints for the ProductController




}
