package com.ecse428.flowfinder.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import com.ecse428.flowfinder.exception.FlowFinderException;
import com.ecse428.flowfinder.model.Client;
import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.model.Registration;
import com.ecse428.flowfinder.model.SpecificClass;
import com.ecse428.flowfinder.service.ClientService;
import com.ecse428.flowfinder.service.RegistrationService;
import com.ecse428.flowfinder.service.SpecificClassService;

@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private ClientService clientService;

    @MockBean
    private SpecificClassService specificClassService;

    private Client client;
    private Instructor instructor;
    private DanceClass danceClass;
    private SpecificClass specificClass;

    @BeforeEach
    public void setUp() {
        client = new Client("client1", "bio1", "client1@email.com", "password1",
                LocalDate.of(2024, Month.SEPTEMBER, 18), false);

        instructor = new Instructor("instructor1", "bio1", "instructor1@email.com", "password1",
                LocalDate.of(2024, Month.SEPTEMBER, 18), false);

        danceClass = new DanceClass(false, "danceClassName1", "danceGenre1", "dance class description 1");

        specificClass = new SpecificClass(false, "Studio A", LocalDate.now().plusDays(10), 12,
                LocalTime.of(10, 0), LocalTime.of(11, 0), danceClass, instructor);
    }

    @Test
    public void testCreateRegistration_success() throws Exception {
        Registration.Key key = new Registration.Key(client, specificClass);
        Registration registration = new Registration(key);

        when(clientService.getClientById(1)).thenReturn(client);
        when(specificClassService.getSpecificClassById(1)).thenReturn(specificClass);
        when(registrationService.createRegistration(client, specificClass)).thenReturn(registration);

        mockMvc.perform(post("/registrations")
                .param("clientId", "1")
                .param("specificClassId", "1"))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateRegistration_missingClientId() throws Exception {
        mockMvc.perform(post("/registrations")
                .param("specificClassId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Client cannot be null"));
    }

    @Test
    public void testCreateRegistration_missingSpecificClassId() throws Exception {
        mockMvc.perform(post("/registrations")
                .param("clientId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Specific Class cannot be null"));
    }

    @Test
    public void testCreateRegistration_clientNotFound() throws Exception {
        when(clientService.getClientById(1)).thenThrow(
                new FlowFinderException(HttpStatus.BAD_REQUEST, "Client not found"));

        mockMvc.perform(post("/registrations")
                .param("clientId", "1")
                .param("specificClassId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Client not found"));
    }

    @Test
    public void testCreateRegistration_classNotFound() throws Exception {
        when(clientService.getClientById(1)).thenReturn(client);
        when(specificClassService.getSpecificClassById(1)).thenThrow(
                new FlowFinderException(HttpStatus.BAD_REQUEST, "Class not found"));

        mockMvc.perform(post("/registrations")
                .param("clientId", "1")
                .param("specificClassId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Class not found"));
    }

    @Test
    public void testCreateRegistration_alreadyRegistered() throws Exception {
        when(clientService.getClientById(1)).thenReturn(client);
        when(specificClassService.getSpecificClassById(1)).thenReturn(specificClass);
        when(registrationService.createRegistration(client, specificClass)).thenThrow(
                new FlowFinderException(HttpStatus.BAD_REQUEST, "Client is already registered for this class"));

        mockMvc.perform(post("/registrations")
                .param("clientId", "1")
                .param("specificClassId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Client is already registered for this class"));
    }

    @Test
    public void testCreateRegistration_classFull() throws Exception {
        when(clientService.getClientById(1)).thenReturn(client);
        when(specificClassService.getSpecificClassById(1)).thenReturn(specificClass);
        when(registrationService.createRegistration(client, specificClass)).thenThrow(
                new FlowFinderException(HttpStatus.BAD_REQUEST, "This class is full"));

        mockMvc.perform(post("/registrations")
                .param("clientId", "1")
                .param("specificClassId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("This class is full"));
    }

    // Deletion/Cancellation Tests
    @Test
    public void testDeleteRegistration_success() throws Exception {
        doNothing().when(registrationService).deleteRegistration(1, 1);

        mockMvc.perform(delete("/registrations")
                .param("clientId", "1")
                .param("specificClassId", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteRegistration_clientNotFound() throws Exception {
        doThrow(new FlowFinderException(HttpStatus.BAD_REQUEST, "Client not found with id: 1"))
                .when(registrationService).deleteRegistration(1, 1);

        mockMvc.perform(delete("/registrations")
                .param("clientId", "1")
                .param("specificClassId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Client not found with id: 1"));
    }

    @Test
    public void testDeleteRegistration_classNotFound() throws Exception {
        doThrow(new FlowFinderException(HttpStatus.BAD_REQUEST, "Class not found with id: 1"))
                .when(registrationService).deleteRegistration(1, 1);

        mockMvc.perform(delete("/registrations")
                .param("clientId", "1")
                .param("specificClassId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Class not found with id: 1"));
    }

    @Test
    public void testDeleteRegistration_registrationNotFound() throws Exception {
        doThrow(new FlowFinderException(HttpStatus.BAD_REQUEST, "Registration does not exist."))
                .when(registrationService).deleteRegistration(1, 1);

        mockMvc.perform(delete("/registrations")
                .param("clientId", "1")
                .param("specificClassId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Registration does not exist."));
    }

    @Test
    public void testDeleteRegistration_internalServerError() throws Exception {
        doThrow(new RuntimeException("Unexpected error"))
                .when(registrationService).deleteRegistration(anyInt(), anyInt());

        mockMvc.perform(delete("/registrations")
                .param("clientId", "1")
                .param("specificClassId", "1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred"));
    }
}
