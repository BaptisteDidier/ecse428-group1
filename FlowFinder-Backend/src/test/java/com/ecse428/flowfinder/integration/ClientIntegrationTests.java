package com.ecse428.flowfinder.integration;

import com.ecse428.flowfinder.model.Client;
import com.ecse428.flowfinder.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private com.ecse428.flowfinder.repository.RegistrationRepository registrationRepository;

    @BeforeEach
    public void setup() {
        // Clean registrations first to avoid foreign key violations, then clients
        registrationRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void ST001_01_testRegisterClient_Successful() throws Exception {
        mockMvc.perform(post("/clients")
                .param("name", "Deniz")
                .param("bio", "Enjoys hip hop")
                .param("email", "deniz@email.com")
                .param("password", "12345678")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Deniz"))
                .andExpect(jsonPath("$.email").value("deniz@email.com"));
    }

    @Test
    public void ST001_02_testRegisterClient_DuplicateEmail() throws Exception {
        // first create a client
        clientRepository.save(new Client("Alice", "Loves salsa", "alice@email.com", "pass12345", java.time.LocalDate.now(), false));

        mockMvc.perform(post("/clients")
                .param("name", "Alice")
                .param("bio", "Duplicate")
                .param("email", "alice@email.com")
                .param("password", "anotherPass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void ST001_03_testRegisterClient_InvalidEmail() throws Exception {
        mockMvc.perform(post("/clients")
                .param("name", "Bob")
                .param("bio", "Loves jazz")
                .param("email", "invalidEmail")
                .param("password", "12345678")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void ST001_04_testRegisterClient_NoBio() throws Exception {
        mockMvc.perform(post("/clients")
                .param("name", "Tom")
                .param("email", "tom@email.com")
                .param("password", "safePass123")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tom"))
                .andExpect(jsonPath("$.bio").doesNotExist());
    }

    @Test
    public void ST002_01_testDeleteClient_Successful() throws Exception {
        Client client = clientRepository.save(new Client("Alice", "Loves salsa", "alice@email.com", "pass12345", java.time.LocalDate.now(), false));

        mockMvc.perform(delete("/clients/{id}", client.getId()))
                .andExpect(status().isNoContent());

        Client deleted = clientRepository.findClientById(client.getId());
        org.junit.jupiter.api.Assertions.assertTrue(deleted.getIsDeleted());
    }

    @Test
    public void ST002_02_testDeleteClient_NotFound() throws Exception {
        mockMvc.perform(delete("/clients/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(content().string("There is no account with id 999"));
    }

    @Test
    public void ST002_03_testDeleteClient_AlreadyDeleted() throws Exception {
        Client client = clientRepository.save(new Client("Bob", "Deleted already", "bob@email.com", "pass12345", java.time.LocalDate.now(), true));

        mockMvc.perform(delete("/clients/{id}", client.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The account is already deleted"));
    }
}
