package org.example.task_bank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.task_bank.entities.Client;
import org.example.task_bank.entities.Client.LegalForm;
import org.example.task_bank.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClientControllerTests {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllClients_returnsClients() throws Exception {
        Client client1 = new Client();
        client1.setId(1L);
        client1.setName("Мордатая мартышка");
        client1.setShortName("Мартышка");
        client1.setAddress("ул. Ленина 123");
        client1.setLegalForm(LegalForm.LLC);

        Client client2 = new Client();
        client2.setId(2L);
        client2.setName("Шаловливая цапля");
        client2.setShortName("Цапля");
        client2.setAddress("ул. Никулина 321");
        client2.setLegalForm(LegalForm.LLC);

        List<Client> clients = Arrays.asList(client1, client2);

        Mockito.when(clientService.getAllClients(null, null, "id", "asc"))
                .thenReturn(clients);

        mockMvc.perform(get("/api/clients")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].name").value("Шаловливая цапля"));
    }


    @Test
    void getClientById_returnsClient() throws Exception {
        Client client = new Client();
        client.setId(1L);
        client.setName("Мордатая мартышка");
        client.setShortName("Мартышка");
        client.setAddress("ул. Ленина 123");
        client.setLegalForm(LegalForm.LLC);

        Mockito.when(clientService.getClientById(1L))
                .thenReturn(ResponseEntity.of(Optional.of(client)));

        mockMvc.perform(get("/api/clients/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Мордатая мартышка"));
    }

    @Test
    void createClient_createsClient() throws Exception {
        Client client = new Client();
        client.setId(1L);
        client.setName("Мордатая мартышка");
        client.setShortName("Мартышка");
        client.setAddress("ул. Ленина 123");
        client.setLegalForm(LegalForm.LLC);


        Mockito.when(clientService.createClient(any(Client.class)))
                .thenReturn(client);

        mockMvc.perform(post("/api/clients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Мордатая мартышка"));
    }

    @Test
    void updateClient_updatesClient() throws Exception {
        Client updatedClient = new Client();
        updatedClient.setId(1L);
        updatedClient.setName("Мордатая мартышка");
        updatedClient.setShortName("Мартышка");
        updatedClient.setAddress("ул. Ленина 123");
        updatedClient.setLegalForm(LegalForm.JSC);

        Mockito.when(clientService.updateClient(eq(1L), any(Client.class)))
                .thenReturn(ResponseEntity.ok(updatedClient));

        mockMvc.perform(put("/api/clients/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Мордатая мартышка"));
    }

    @Test
    void deleteClient_deletesClient() throws Exception {
        Mockito.when(clientService.deleteClient(1L))
                .thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/api/clients/1")
                        .contentType("application/json"))
                .andExpect(status().isNoContent());
    }
}
