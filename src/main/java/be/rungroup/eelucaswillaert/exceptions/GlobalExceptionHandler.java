package be.rungroup.eelucaswillaert.exceptions;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

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

}
