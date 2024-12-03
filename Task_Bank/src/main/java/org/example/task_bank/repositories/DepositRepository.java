package org.example.task_bank.repositories;

import org.example.task_bank.entities.Deposit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {

    // Найти все вклады клиента с сортировкой
    List<Deposit> findByClientId(Long clientId, Sort sort);

    // Найти все вклады банка с сортировкой
    List<Deposit> findByBankId(Long bankId, Sort sort);

    // Найти вклады по клиенту и банку с сортировкой
    List<Deposit> findByClientIdAndBankId(Long clientId, Long bankId, Sort sort);
}
