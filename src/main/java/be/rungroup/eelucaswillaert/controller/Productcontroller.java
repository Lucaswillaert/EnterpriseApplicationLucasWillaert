package be.rungroup.eelucaswillaert.controller;

import be.rungroup.eelucaswillaert.dto.ProductDto;
import be.rungroup.eelucaswillaert.exceptions.FileSizeLimitExceededException;
import be.rungroup.eelucaswillaert.model.Product;
import be.rungroup.eelucaswillaert.model.Tag;

import be.rungroup.eelucaswillaert.model.User;
import be.rungroup.eelucaswillaert.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Service

public class Productcontroller {

    @Autowired
    private ProductService productService;

    public Productcontroller(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/product-list")
    public String showAllProducts(Model model, HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        List<ProductDto> products= productService.findAllProducts();
        model.addAttribute("products",products);
        model.addAttribute("tags", Tag.values());
        model.addAttribute("selectedTags", new ArrayList<>()); // Initialize selectedTags to prevent null pointer exception
        model.addAttribute("currentUri", request.getRequestURI());
        return "/products/product-list";
    }

    @GetMapping("/products/new")
    public String createProductForm(Model model, HttpServletRequest request){
        Product product = new Product();
        model.addAttribute("product", product);
        model.addAttribute("tags", Tag.values());
        model.addAttribute("selectedTags", new ArrayList<>());
        model.addAttribute("currentUri", request.getRequestURI());
        return "products/product-create";
    }

    @GetMapping("/products/{id}/photo")
    public ResponseEntity<byte[]> getProductPhoto(@PathVariable Long id) {
        byte[] photoBytes = productService.getProductPhoto(id); // Ophalen van bytes
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG) // Of IMAGE_PNG afhankelijk van je type
                .body(photoBytes);
    }


    @GetMapping("/products/{id:[0-9]+}")
    public String productDetail(@PathVariable long id, Model model, HttpServletRequest request, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        model.addAttribute("user", user);

        ProductDto productDto = productService.findById(id);
        if (productDto == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        model.addAttribute("product", productDto);
        model.addAttribute("currentUri",request.getRequestURI());
        return "products/product-detail";
    }

    @PostMapping("/products/new")
    public String saveProduct(@ModelAttribute ProductDto productDto, BindingResult result, Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("product", productDto);
            return "/products/product-create";
        }
        try {
            if (productDto.getPhoto() != null && !productDto.getPhoto().isEmpty()) {
                if (productDto.getPhoto().getSize() > 10 * 1024 * 1024) { // 10 MB
                    throw new FileSizeLimitExceededException("File size exceeds the 10 MB limit.");
                }
                productDto.setPhotoBytes(productDto.getPhoto().getBytes());
            }
        } catch (IOException e) {
            model.addAttribute("error", "Error uploading file");
            return "/products/product-create";
        }
        productService.saveProduct(productDto);
        return "redirect:/products/product-list";
    }


    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product successfully deleted.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/products/product-list";
    }

    @GetMapping("/products/search")
    public String SearchCatalogue(@RequestParam(value="query")String query, Model model, HttpServletRequest request){
        List<ProductDto> products = productService.searchProducts(query);
        model.addAttribute("products",products);
        model.addAttribute("tags", Tag.values());
        model.addAttribute("selectedTags", new ArrayList<>());
        model.addAttribute("currentUri", request.getRequestURI());
        return "/products/product-list";
    }

    @GetMapping("/products/filter")
    public String filterProducts(@RequestParam(value="tags", required = false) List<String> tags, Model model, HttpServletRequest request){
        List<ProductDto> products;
        if(tags == null || tags.isEmpty()){
            products = productService.findAllProducts();
        } else {
            products = productService.filterProductsByTags(tags);
        }
        model.addAttribute("products", products);
        model.addAttribute("tags", Tag.values());
        model.addAttribute("selectedTags", tags != null ? tags : new ArrayList<>());
        model.addAttribute("currentUri", request.getRequestURI());
        return "/products/product-list";
    }












}
