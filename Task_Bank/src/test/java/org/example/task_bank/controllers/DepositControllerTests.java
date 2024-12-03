package org.example.task_bank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.task_bank.dto.DepositDTO;
import org.example.task_bank.entities.Bank;
import org.example.task_bank.entities.Client;
import org.example.task_bank.entities.Deposit;
import org.example.task_bank.services.DepositService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DepositControllerTests {

    @Mock
    private DepositService depositService;

    @InjectMocks
    private DepositController depositController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(depositController).build();
    }

    @Test
    void getAllDeposits_returnsDeposits() throws Exception {
        DepositDTO depositDTO1 = new DepositDTO();
        depositDTO1.setId(1L);
        depositDTO1.setClientId(1L);
        depositDTO1.setBankId(1L);
        depositDTO1.setOpenDate(LocalDate.of(2023, 1, 1));
        depositDTO1.setInterestRate(5.0);
        depositDTO1.setTermInMonths(12);

        DepositDTO depositDTO2 = new DepositDTO();
        depositDTO2.setId(2L);
        depositDTO2.setClientId(2L);
        depositDTO2.setBankId(3L);
        depositDTO2.setOpenDate(LocalDate.of(2024, 12, 11));
        depositDTO2.setInterestRate(3.5);
        depositDTO2.setTermInMonths(8);

        List<DepositDTO> deposits = Arrays.asList(depositDTO1, depositDTO2);

        Mockito.when(depositService.getAllDeposits(null, null, "id", "asc"))
                .thenReturn(deposits);

        mockMvc.perform(get("/api/deposits")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // Ожидаем 2 депозита
                .andExpect(jsonPath("$[1].clientId").value(2L)); // Проверяем clientId второго депозита
    }


    @Test
    void getDepositById_returnsDeposit() throws Exception {
        DepositDTO depositDTO = new DepositDTO();
        depositDTO.setId(1L);
        depositDTO.setClientId(1L);
        depositDTO.setBankId(1L);
        depositDTO.setOpenDate(LocalDate.of(2023, 1, 1));
        depositDTO.setInterestRate(5.0);
        depositDTO.setTermInMonths(12);

        Mockito.when(depositService.getDepositById(1L))
                .thenReturn(ResponseEntity.ok(depositDTO));

        mockMvc.perform(get("/api/deposits/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.clientId").value(1L))
                .andExpect(jsonPath("$.bankId").value(1L));
    }

    @Test
    void createDeposit_createsDeposit() throws Exception {
        Client client = new Client();
        client.setId(3L);

        Bank bank=new Bank();
        bank.setId(2L);

        Deposit deposit = new Deposit();
        deposit.setId(1L);
        deposit.setClient(client);
        deposit.setBank(bank);
        deposit.setOpenDate(LocalDate.of(2023, 1, 1));
        deposit.setInterestRate(5.0);
        deposit.setTermInMonths(12);

        Mockito.when(depositService.createDeposit(any(Deposit.class)))
                .thenReturn(deposit);

        mockMvc.perform(post("/api/deposits")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(deposit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.client.id").value(3L));
    }

    @Test
    void updateDeposit_updatesDeposit() throws Exception {
        Client client = new Client();
        client.setId(3L);

        Bank bank=new Bank();
        bank.setId(2L);

        Deposit updatedDeposit = new Deposit();
        updatedDeposit.setId(1L);
        updatedDeposit.setClient(client);
        updatedDeposit.setBank(bank);
        updatedDeposit.setOpenDate(LocalDate.of(2023, 2, 1));
        updatedDeposit.setInterestRate(6.0);
        updatedDeposit.setTermInMonths(24);

        Mockito.when(depositService.updateDeposit(eq(1L), any(Deposit.class)))
                .thenReturn(ResponseEntity.ok(updatedDeposit));

        mockMvc.perform(put("/api/deposits/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedDeposit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.bank.id").value(2L))
                .andExpect(jsonPath("$.interestRate").value(6.0));
    }

    @Test
    void deleteDeposit_deletesDeposit() throws Exception {
        Mockito.when(depositService.deleteDeposit(1L))
                .thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/api/deposits/1")
                        .contentType("application/json"))
                .andExpect(status().isNoContent());
    }
}
