package be.rungroup.eelucaswillaert.exceptions;


import be.rungroup.eelucaswillaert.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ProductRepository productRepository;


    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model){
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public String handleFileSizeLimitExceededException(FileSizeLimitExceededException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(DateValidationException.class)
    public String handleDateValidationException(DateValidationException ex, Model model, @PathVariable Long product_id) {
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("product", productRepository.findById(product_id)
                .orElseThrow(() -> new IllegalArgumentException("Product niet gevonden")));
        return "products/product-detail";
    }

}
