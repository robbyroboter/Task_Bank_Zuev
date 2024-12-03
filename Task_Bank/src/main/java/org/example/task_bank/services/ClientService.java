package org.example.task_bank.services;

import org.example.task_bank.entities.Client;
import org.example.task_bank.repositories.ClientRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients(String name, String legalForm, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        if (name != null && legalForm != null) {
            return clientRepository.findByNameContainingAndLegalForm(name, Client.LegalForm.valueOf(legalForm), sort);
        } else if (name != null) {
            return clientRepository.findByNameContaining(name, sort);
        } else if (legalForm != null) {
            return clientRepository.findByLegalForm(Client.LegalForm.valueOf(legalForm), sort);
        } else {
            return clientRepository.findAll(sort);
        }
    }

    public ResponseEntity<Client> getClientById(Long id) {
        return clientRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public Client createClient(Client client) {
        if (clientRepository.existsByName(client.getName())) {
            throw new IllegalArgumentException("Клиент с таким названием существует");
        }
        return clientRepository.save(client);
    }

    public ResponseEntity<Client> updateClient(Long id, Client updatedClient) {
        if (clientRepository.existsByName(updatedClient.getName())) {
            throw new IllegalArgumentException("Клиент с таким названием существует");
        }
        return clientRepository.findById(id).map(client -> {
            client.setName(updatedClient.getName());
            client.setShortName(updatedClient.getShortName());
            client.setAddress(updatedClient.getAddress());
            client.setLegalForm(updatedClient.getLegalForm());
            return ResponseEntity.ok(clientRepository.save(client));
        }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> deleteClient(Long id) {
        return clientRepository.findById(id).map(client -> {
            clientRepository.delete(client);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
