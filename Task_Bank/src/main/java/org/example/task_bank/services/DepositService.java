package org.example.task_bank.services;

import org.example.task_bank.dto.DepositDTO;
import org.example.task_bank.entities.Deposit;
import org.example.task_bank.repositories.BankRepository;
import org.example.task_bank.repositories.ClientRepository;
import org.example.task_bank.repositories.DepositRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepositService {

    private final DepositRepository depositRepository;

    private final BankRepository bankRepository;

    private final ClientRepository clientRepository;

    public DepositService(DepositRepository depositRepository, BankRepository bankRepository, ClientRepository clientRepository) {
        this.depositRepository = depositRepository;
        this.bankRepository = bankRepository;
        this.clientRepository = clientRepository;
    }


    public List<DepositDTO> getAllDeposits(Long clientId, Long bankId, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        List<Deposit> deposits;

        if (clientId != null && bankId != null) {
            deposits = depositRepository.findByClientIdAndBankId(clientId, bankId, sort);
        } else if (clientId != null) {
            deposits = depositRepository.findByClientId(clientId, sort);
        } else if (bankId != null) {
            deposits = depositRepository.findByBankId(bankId, sort);
        } else {
            deposits = depositRepository.findAll(sort);
        }

        return deposits.stream()
                .map(deposit -> {
                    DepositDTO depositDTO = new DepositDTO();
                    depositDTO.setId(deposit.getId());
                    depositDTO.setClientId(deposit.getClient().getId());
                    depositDTO.setBankId(deposit.getBank().getId());
                    depositDTO.setOpenDate(deposit.getOpenDate());
                    depositDTO.setInterestRate(deposit.getInterestRate());
                    depositDTO.setTermInMonths(deposit.getTermInMonths());
                    return depositDTO;
                })
                .collect(Collectors.toList());
    }


    public ResponseEntity<DepositDTO> getDepositById(Long id) {

        Deposit deposit = depositRepository.findById(id).orElse(null);

        if (deposit == null) {
            return ResponseEntity.notFound().build();
        }

        DepositDTO depositDTO = new DepositDTO();
        depositDTO.setId(deposit.getId());
        depositDTO.setClientId(deposit.getClient().getId());
        depositDTO.setBankId(deposit.getBank().getId());
        depositDTO.setOpenDate(deposit.getOpenDate());
        depositDTO.setInterestRate(deposit.getInterestRate());
        depositDTO.setTermInMonths(deposit.getTermInMonths());

        return ResponseEntity.ok(depositDTO);
    }


    public Deposit createDeposit(Deposit deposit) {
        clientRepository.findById(deposit.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Клиент с ID " + deposit.getClient().getId() + " не найден"));
        bankRepository.findById(deposit.getBank().getId())
                .orElseThrow(() -> new IllegalArgumentException("Банк с ID " + deposit.getBank().getId() + " не найден"));
        return depositRepository.save(deposit);
    }

    public ResponseEntity<Deposit> updateDeposit(Long id, Deposit updatedDeposit) {

        return depositRepository.findById(id).map(deposit -> {
            deposit.setClient(updatedDeposit.getClient());
            deposit.setBank(updatedDeposit.getBank());
            deposit.setOpenDate(updatedDeposit.getOpenDate());
            deposit.setInterestRate(updatedDeposit.getInterestRate());
            deposit.setTermInMonths(updatedDeposit.getTermInMonths());
            return ResponseEntity.ok(depositRepository.save(deposit));
        }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> deleteDeposit(Long id) {
        return depositRepository.findById(id).map(deposit -> {
            depositRepository.delete(deposit);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}