package com.ecse428.flowfinder.integration;

import com.ecse428.flowfinder.model.Client;
import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.model.SpecificClass;
import com.ecse428.flowfinder.repository.ClientRepository;
import com.ecse428.flowfinder.repository.DanceClassRepository;
import com.ecse428.flowfinder.repository.InstructorRepository;
import com.ecse428.flowfinder.repository.RegistrationRepository;
import com.ecse428.flowfinder.repository.SpecificClassRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private DanceClassRepository danceClassRepository;

    @Autowired
    private SpecificClassRepository specificClassRepository;

    @BeforeEach
    public void setup() {
        registrationRepository.deleteAll();
        specificClassRepository.deleteAll();
        danceClassRepository.deleteAll();
        instructorRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void ST012_01_testSuccessfulRegistration() throws Exception {
        Client client = clientRepository.save(new Client("client1", "Bio1", "client1@email.com", "pass1234", LocalDate.now(), false));
        Instructor instructor = instructorRepository.save(new Instructor("instructor1", "BioI", "instructor1@email.com", "pass1234", LocalDate.now(), false));
        DanceClass danceClass = danceClassRepository.save(new DanceClass(false, "danceClassName1", "danceGenre1", "desc"));
        SpecificClass specificClass = specificClassRepository.save(new SpecificClass(false, "Studio A", LocalDate.now().plusDays(1), 12, LocalTime.of(10, 0), LocalTime.of(11, 0), danceClass, instructor));

        mockMvc.perform(post("/registrations")
                .param("clientId", String.valueOf(client.getId()))
                .param("specificClassId", String.valueOf(specificClass.getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.key.participant.id").value(client.getId()))
                .andExpect(jsonPath("$.key.danceClass.id").value(specificClass.getId()));
    }

    @Test
    public void ST012_02_testBookingFailsIfClientNull() throws Exception {
        Instructor instructor = instructorRepository.save(new Instructor("instructor1", "BioI", "instructor1@email.com", "pass1234", LocalDate.now(), false));
        DanceClass danceClass = danceClassRepository.save(new DanceClass(false, "danceClassName1", "danceGenre1", "desc"));
        SpecificClass specificClass = specificClassRepository.save(new SpecificClass(false, "Studio A", LocalDate.now().plusDays(1), 12, LocalTime.of(10, 0), LocalTime.of(11, 0), danceClass, instructor));

        mockMvc.perform(post("/registrations")
                .param("specificClassId", String.valueOf(specificClass.getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Client cannot be null"));
    }

    @Test
    public void ST012_03_testBookingFailsIfClassNull() throws Exception {
        Client client = clientRepository.save(new Client("client1", "Bio1", "client1@email.com", "pass1234", LocalDate.now(), false));

        mockMvc.perform(post("/registrations")
                .param("clientId", String.valueOf(client.getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Specific Class cannot be null"));
    }

    @Test
    public void ST012_04_testBookingFailsIfAlreadyRegistered() throws Exception {
        Client client = clientRepository.save(new Client("client1", "Bio1", "client1@email.com", "pass1234", LocalDate.now(), false));
        Instructor instructor = instructorRepository.save(new Instructor("instructor1", "BioI", "instructor1@email.com", "pass1234", LocalDate.now(), false));
        DanceClass danceClass = danceClassRepository.save(new DanceClass(false, "danceClassName1", "danceGenre1", "desc"));
        SpecificClass specificClass = specificClassRepository.save(new SpecificClass(false, "Studio A", LocalDate.now().plusDays(1), 12, LocalTime.of(10, 0), LocalTime.of(11, 0), danceClass, instructor));

        mockMvc.perform(post("/registrations")
                .param("clientId", String.valueOf(client.getId()))
                .param("specificClassId", String.valueOf(specificClass.getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/registrations")
                .param("clientId", String.valueOf(client.getId()))
                .param("specificClassId", String.valueOf(specificClass.getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Client is already registered for this class"));
    }

    @Test
    public void ST012_05_testBookingFailsIfClassFull() throws Exception {
        Client client = clientRepository.save(new Client("client1", "Bio1", "client1@email.com", "pass1234", LocalDate.now(), false));
        Instructor instructor = instructorRepository.save(new Instructor("instructor1", "BioI", "instructor1@email.com", "pass1234", LocalDate.now(), false));
        DanceClass danceClass = danceClassRepository.save(new DanceClass(false, "danceClassName1", "danceGenre1", "desc"));
        SpecificClass specificClass = specificClassRepository.save(new SpecificClass(false, "Studio A", LocalDate.now().plusDays(1), 12, LocalTime.of(10, 0), LocalTime.of(11, 0), danceClass, instructor));

        Client client2 = clientRepository.save(new Client("client2", "Bio2", "client2@email.com", "pass1234", LocalDate.now(), false));
        mockMvc.perform(post("/registrations")
                .param("clientId", String.valueOf(client2.getId()))
                .param("specificClassId", String.valueOf(specificClass.getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/registrations")
                .param("clientId", String.valueOf(client.getId()))
                .param("specificClassId", String.valueOf(specificClass.getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("This class is full"));
    }

}
