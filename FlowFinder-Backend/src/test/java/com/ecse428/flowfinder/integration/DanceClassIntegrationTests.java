package com.ecse428.flowfinder.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.ecse428.flowfinder.exception.FlowFinderException;
import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.model.SpecificClass;
import com.ecse428.flowfinder.repository.DanceClassRepository;
import com.ecse428.flowfinder.repository.InstructorRepository;
import com.ecse428.flowfinder.repository.RegistrationRepository;
import com.ecse428.flowfinder.repository.SpecificClassRepository;
import com.ecse428.flowfinder.service.DanceClassService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest
@AutoConfigureMockMvc
public class DanceClassIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DanceClassService danceClassService;

    @Autowired
    private DanceClassRepository danceClassRepository;

    @Autowired
    private SpecificClassRepository specificClassRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        registrationRepository.deleteAll();
        specificClassRepository.deleteAll();
        danceClassRepository.deleteAll();
        instructorRepository.deleteAll();

        // Create instructor used in background for ID_005 tests
        Instructor instructor = new Instructor("instructor", "Bio", "instructor@email.com", "pass123", LocalDate.now(), false);
        instructorRepository.save(instructor);

        // Background classes for ID_005 tests
        danceClassRepository.save(new DanceClass(true, "Salsa 101", "Salsa", "Basic Salsa Techniques"));
        danceClassRepository.save(new DanceClass(false, "Brekdancing 252", "Break", "Introduction to intermediate breakdancing techniques"));
    }

    // ST006 – Normal flow
    @Test
    public void ST006_01_deleteDanceClass_success_whenNoSpecificClasses() {
        DanceClass hipHop = new DanceClass(false, "Hip Hop Fusion", "Hip Hop", "Hip hop class");
        hipHop = danceClassRepository.save(hipHop);

        assertTrue(danceClassRepository.findById(hipHop.getId()).isPresent());
        DanceClass deleted = danceClassService.deleteDanceClass("Hip Hop Fusion");

        // Assert
        assertNotNull(deleted);
        assertEquals("Hip Hop Fusion", deleted.getName());
        assertFalse(danceClassRepository.findById(hipHop.getId()).isPresent(),
                "Dance class should be removed from the repository");
    }

    // ST006 – Alternate flow
    @Test
    public void ST006_02_deleteDanceClass_fails_whenSpecificClassesExist() {
        DanceClass salsa = new DanceClass(false, "Beginner Salsa", "Salsa", "Intro salsa");
        salsa = danceClassRepository.save(salsa);
        SpecificClass sc = new SpecificClass (false, "R101", LocalDate.of(2025, 12, 10), 20, LocalTime.of(18, 0), LocalTime.of(19, 0), salsa, null);
        sc.setDanceClass(salsa);
        specificClassRepository.save(sc);

        // Act + Assert
        FlowFinderException ex = assertThrows(
                FlowFinderException.class,
                () -> danceClassService.deleteDanceClass("Beginner Salsa"),
                "Deleting a class with specific classes should throw FlowFinderException");

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus(),
                "Status should be 400 BAD_REQUEST when specific classes exist");
        assertTrue(ex.getMessage().contains("Cannot delete") 
                        || ex.getMessage().contains("Cannot remove"),
                "Error message should indicate we cannot delete class with existing specific classes");

        // And the class should still be in the DB
        assertTrue(danceClassRepository.findById(salsa.getId()).isPresent(),
                "Dance class should remain in repository");
    }

    //ST006 – Error flow
    @Test
    public void ST006_03_deleteDanceClass_nonExistingClass_throwsNotFound() {
        // No classes in DB

        FlowFinderException ex = assertThrows(
                FlowFinderException.class,
                () -> danceClassService.deleteDanceClass("Vogue"),
                "Deleting a non-existent class should throw FlowFinderException");

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus(),
                "Status should be 404 NOT_FOUND for non-existent class");

        // Optional: check message
        assertTrue(ex.getMessage().contains("does not exist")
                        || ex.getMessage().contains("Class not found"),
                "Error message should indicate the class does not exist");
    }

    @Test
    public void ID_005_01_successfullyAddingNewClass() throws Exception {
        Map<String,Object> payload = new HashMap<>();
        payload.put("isPrivate", true);
        payload.put("name", "Tap Dancing 101");
        payload.put("genre", "Tap");
        payload.put("description", "Basic Tap Dancing");
        String json = mapper.writeValueAsString(payload);

        mockMvc.perform(post("/dance-classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tap Dancing 101"));

        // verify repository contains the new class
        boolean found = false;
        for (DanceClass dc : danceClassRepository.findAll()) {
            if ("Tap Dancing 101".equals(dc.getName())) {
                found = true;
                break;
            }
        }
        assert found : "Class 'Tap Dancing 101' should exist in repository";
    }

    @Test
    public void ID_005_02_duplicateNameReturnsError() throws Exception {
        Map<String,Object> payload = new HashMap<>();
        payload.put("isPrivate", false);
        payload.put("name", "Salsa 101");
        payload.put("genre", "Salsa");
        payload.put("description", "Basic Salsa-ing");
        String json = mapper.writeValueAsString(payload);

        mockMvc.perform(post("/dance-classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("already exists")));
    }

    @Test
    public void ID_005_03_missingNameReturnsValidationError() throws Exception {
        // omit name
        Map<String,Object> payload = new HashMap<>();
        payload.put("isPrivate", true);
        payload.put("genre", "Conga");
        payload.put("description", "Basic Conga Lines");
        String json = mapper.writeValueAsString(payload);

        mockMvc.perform(post("/dance-classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Class name is required")));
    }

    @Test
    public void ID_005_04_missingDescriptionReturnsValidationError() throws Exception {
        Map<String,Object> payload = new HashMap<>();
        payload.put("isPrivate", false);
        payload.put("name", "Ballet 438");
        payload.put("genre", "Ballet");
        String json = mapper.writeValueAsString(payload);

        mockMvc.perform(post("/dance-classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Description is required")));
    }

    @Test
    public void ID_005_05_missingClassTypeReturnsValidationError() throws Exception {
        // omit isPrivate
        Map<String,Object> payload = new HashMap<>();
        payload.put("name", "Hip Hop 101");
        payload.put("genre", "Hip Hop");
        payload.put("description", "Introduction to Hip Hop");
        String json = mapper.writeValueAsString(payload);

        mockMvc.perform(post("/dance-classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Class type is required")));
    }
}
