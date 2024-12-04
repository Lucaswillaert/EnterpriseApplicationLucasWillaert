package be.rungroup.eelucaswillaert.controller;

import be.rungroup.eelucaswillaert.dto.ProductDto;
import be.rungroup.eelucaswillaert.model.Product;
import be.rungroup.eelucaswillaert.model.Tag;
import be.rungroup.eelucaswillaert.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Service
public class Productcontroller {


    //TODO: aan te maken templates: product-detail.html, product-create.html, product-edit.html
    @Autowired
    private ProductService productService;

    public Productcontroller(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String showAllProducts(Model model) {
        List<ProductDto> products= productService.findAllProducts();
        model.addAttribute("products",products);

        return "/products/product-list";
    }
    //TODO: add ALL API endpoints for the ProductController

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable long id, Model model) {
        ProductDto productDto = productService.findById(id);
        if (productDto == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        model.addAttribute("product", productDto);
        return "products/product-detail";
    }

    @GetMapping("/products/new")
    public String createProductForm(Model model){
        Product product = new Product();
        model.addAttribute("product", product);
        model.addAttribute("tags", Tag.values());
        return "products/product-create";
    }

    @PostMapping("/products/new")
    public String saveProduct(@ModelAttribute ProductDto productDto , BindingResult result, Model model ){
        if(result.hasErrors()){
            model.addAttribute("product",productDto);
            return "/products/product-create";
        }
        else {
            productService.saveProduct(productDto);
            return "redirect:/products";
        }

    }

    @GetMapping("/products/{id}/edit")
    public String editProductForm(@PathVariable long id, Model model ){
        ProductDto productDto = productService.findById(id);
        model.addAttribute("product",productDto);
        model.addAttribute("tags", Tag.values());
        return "products/product-edit";
    }


    @PostMapping("/products/{id}/edit")
    public String editProduct(@ModelAttribute ProductDto productDto, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("product",productDto);
            return "products/product-edit";
        }
        else {
            productService.updateProduct(productDto);
            return "redirect:/products";
        }
    }

    @DeleteMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable long id){
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    @GetMapping("/products/search")
    public String SearchCatalogue(@RequestParam(value="query")String query, Model model){
        List<ProductDto> products = productService.searchProducts(query);
        model.addAttribute("products",products);
        return "product-list";
    }












}
