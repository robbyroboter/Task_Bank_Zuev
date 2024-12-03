package org.example.task_bank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.task_bank.entities.Bank;
import org.example.task_bank.services.BankService;
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
class BankControllerTests {

    @Mock
    private BankService bankService;

    @InjectMocks
    private BankController bankController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bankController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllBanks_returnsBanks() throws Exception {
        Bank bank1 = new Bank();
        bank1.setId(1L);
        bank1.setName("Банк Топтышка");
        bank1.setBic("123456789");

        Bank bank2 = new Bank();
        bank2.setId(2L);
        bank2.setName("Банк Крутышка");
        bank2.setBic("987654321");

        List<Bank> banks = Arrays.asList(bank1, bank2);

        Mockito.when(bankService.getAllBanks(null, "id", "asc"))
                .thenReturn(banks);

        mockMvc.perform(get("/api/banks")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].name").value("Банк Крутышка"));
    }


    @Test
    void getBankById_returnsBank() throws Exception {
        Bank bank = new Bank();
        bank.setId(1L);
        bank.setName("Банк Крутышка");
        bank.setBic("123456789");

        Mockito.when(bankService.getBankById(1L))
                .thenReturn(ResponseEntity.of(Optional.of(bank)));

        mockMvc.perform(get("/api/banks/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Банк Крутышка"));
    }

    @Test
    void createBank_createsBank() throws Exception {
        Bank bank = new Bank();
        bank.setId(1L);
        bank.setName("Банк Топтышка");
        bank.setBic("123456789");

        Mockito.when(bankService.createBank(any(Bank.class)))
                .thenReturn(bank);

        mockMvc.perform(post("/api/banks")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bank)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Банк Топтышка"));
    }

    @Test
    void updateBank_updatesBank() throws Exception {
        Bank updatedBank = new Bank();
        updatedBank.setId(1L);
        updatedBank.setName("Обнова Банк");
        updatedBank.setBic("987654321");

        Mockito.when(bankService.updateBank(eq(1L), any(Bank.class)))
                .thenReturn(ResponseEntity.ok(updatedBank));

        mockMvc.perform(put("/api/banks/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedBank)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Обнова Банк"));
    }

    @Test
    void deleteBank_deletesBank() throws Exception {
        Mockito.when(bankService.deleteBank(1L))
                .thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/api/banks/1")
                        .contentType("application/json"))
                .andExpect(status().isNoContent());
    }
}
