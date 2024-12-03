package org.example.task_bank.repositories;

import org.example.task_bank.entities.Bank;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

    boolean existsByBic(String bic);

    boolean existsByName(String name);

    // Поиск банков по подстроке в имени
    List<Bank> findByNameContaining(String name, Sort sort);
}
