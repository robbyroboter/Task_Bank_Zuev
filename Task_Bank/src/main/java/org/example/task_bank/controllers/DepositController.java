package org.example.task_bank.controllers;

import jakarta.transaction.Transactional;
import org.example.task_bank.dto.DepositDTO;
import org.example.task_bank.entities.Deposit;
import org.example.task_bank.services.DepositService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/deposits")
public class DepositController {

    private final DepositService depositService;

    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @GetMapping
    public List<DepositDTO> getAllDeposits(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long bankId,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        return depositService.getAllDeposits(clientId, bankId, sortBy, sortDirection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepositDTO> getDepositById(@PathVariable Long id) {
        return depositService.getDepositById(id);
    }


    @PostMapping
    public Deposit createDeposit(@RequestBody Deposit deposit) {
        return depositService.createDeposit(deposit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deposit> updateDeposit(@PathVariable Long id, @RequestBody Deposit updatedDeposit) {
        return depositService.updateDeposit(id, updatedDeposit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDeposit(@PathVariable Long id) {
        return depositService.deleteDeposit(id);
    }
}
