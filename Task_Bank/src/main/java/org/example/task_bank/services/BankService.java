package org.example.task_bank.services;

import org.example.task_bank.entities.Bank;
import org.example.task_bank.repositories.BankRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {

    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public List<Bank> getAllBanks(String name, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        if (name != null) {
            return bankRepository.findByNameContaining(name, sort);
        } else {
            return bankRepository.findAll(sort);
        }
    }

    public ResponseEntity<Bank> getBankById(Long id) {
        return bankRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public Bank createBank(Bank bank) {
        if (bankRepository.existsByBic(bank.getBic()) || bankRepository.existsByName(bank.getName())) {
            throw new IllegalArgumentException("Банк с таким БИК или названием существует");
        } else if (bankRepository.existsByBic(bank.getBic()) && bankRepository.existsByName(bank.getName())) {
            throw new IllegalArgumentException("Банк с таким БИК и названием существует");
        }
        return bankRepository.save(bank);
    }

    public ResponseEntity<Bank> updateBank(Long id, Bank updatedBank) {
        if (bankRepository.existsByBic(updatedBank.getBic())|| bankRepository.existsByName(updatedBank.getName())) {
            throw new IllegalArgumentException("Банк с таким БИК или названием существует");
        }
        return bankRepository.findById(id).map(bank -> {
            bank.setName(updatedBank.getName());
            bank.setBic(updatedBank.getBic());
            return ResponseEntity.ok(bankRepository.save(bank));
        }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> deleteBank(Long id) {
        return bankRepository.findById(id).map(bank -> {
            bankRepository.delete(bank);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
