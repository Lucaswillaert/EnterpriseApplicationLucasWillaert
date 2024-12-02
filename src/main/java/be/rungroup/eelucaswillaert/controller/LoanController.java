package be.rungroup.eelucaswillaert.controller;

import be.rungroup.eelucaswillaert.dto.LoanDTO;
import be.rungroup.eelucaswillaert.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/borrow")
    public ResponseEntity<String> borrowProduct(@RequestBody LoanDTO loanDTO) {
        try {
            loanService.borrowProduct(loanDTO);
            return ResponseEntity.ok("Product successfully borrowed!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/return/{loanId}")
    public ResponseEntity<String> returnProduct(@PathVariable Long loanId) {
        loanService.returnProduct(loanId);
        return ResponseEntity.ok("Product successfully returned!");
    }
}
