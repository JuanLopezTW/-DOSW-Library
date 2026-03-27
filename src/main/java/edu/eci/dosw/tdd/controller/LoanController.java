package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.controller.mapper.LoanMapper;
import edu.eci.dosw.tdd.core.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;
    private final LoanMapper loanMapper;

    public LoanController(LoanService loanService, LoanMapper loanMapper) {
        this.loanService = loanService;
        this.loanMapper = loanMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'LIBRARIAN')")
    public ResponseEntity<LoanDTO> createLoan(@RequestParam Long userId, @RequestParam Long bookId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loanMapper.toDTO(loanService.createLoan(userId, bookId)));
    }

    @PutMapping("/return")
    @PreAuthorize("hasAnyRole('USER', 'LIBRARIAN')")
    public ResponseEntity<Void> returnLoan(@RequestParam Long userId, @RequestParam Long bookId) {
        loanService.returnLoan(userId, bookId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<LoanDTO> loans = loanService.getAllLoans().stream()
                .map(loanMapper::toDTO)
                .toList();
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'LIBRARIAN')")
    public ResponseEntity<List<LoanDTO>> getLoansByUser(@PathVariable Long userId) {
        List<LoanDTO> loans = loanService.getLoansByUser(userId).stream()
                .map(loanMapper::toDTO)
                .toList();
        return ResponseEntity.ok(loans);
    }
}
