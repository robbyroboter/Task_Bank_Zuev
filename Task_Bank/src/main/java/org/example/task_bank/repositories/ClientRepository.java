package org.example.task_bank.repositories;

import org.example.task_bank.entities.Client;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByName(String name);

    // Поиск клиентов по подстроке в имени и форме организации
    List<Client> findByNameContainingAndLegalForm(String name, Client.LegalForm legalForm, Sort sort);

    // Поиск клиентов только по имени
    List<Client> findByNameContaining(String name, Sort sort);

    // Поиск клиентов только по форме организации
    List<Client> findByLegalForm(Client.LegalForm legalForm, Sort sort);
}
