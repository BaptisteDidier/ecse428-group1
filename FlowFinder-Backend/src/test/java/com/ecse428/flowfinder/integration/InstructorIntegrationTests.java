package com.ecse428.flowfinder.integration;

import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.model.SpecificClass;
import com.ecse428.flowfinder.repository.DanceClassRepository;
import com.ecse428.flowfinder.repository.InstructorRepository;
import com.ecse428.flowfinder.repository.RegistrationRepository;
import com.ecse428.flowfinder.repository.SpecificClassRepository;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.hamcrest.Matchers.is;


import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.allOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class InstructorIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private SpecificClassRepository specificClassRepository;

    @Autowired
    private DanceClassRepository danceClassRepository;

    private SpecificClass beginnerBallet;
    private SpecificClass advancedHipHop;
    private SpecificClass latinBasics;

    @BeforeEach
    public void setup() {
        registrationRepository.deleteAll();
        specificClassRepository.deleteAll();
        danceClassRepository.deleteAll();
        instructorRepository.deleteAll();

        // Create DanceClasses
        DanceClass beginnerBalletDance = danceClassRepository.save(
                new DanceClass(false, "Beginner Ballet", "Ballet", "Intro to basic ballet"));
        DanceClass advancedHipHopDance = danceClassRepository.save(
                new DanceClass(false, "Advanced Hip Hop", "Hip Hop", "Advanced hip hop drills"));
        DanceClass latinBasicsDance = danceClassRepository.save(
                new DanceClass(false, "Latin Basics", "Latin", "Salsa & bachata intro"));

        // Create SpecificClasses
        beginnerBallet = specificClassRepository.save(
                new SpecificClass(false, "Studio A", LocalDate.of(2025, 11, 3), 20,
                        LocalTime.of(18, 0), LocalTime.of(19, 0), beginnerBalletDance, null));
        advancedHipHop = specificClassRepository.save(
                new SpecificClass(false, "Studio B", LocalDate.of(2025, 11, 5), 25,
                        LocalTime.of(19, 30), LocalTime.of(20, 45), advancedHipHopDance, null));
        latinBasics = specificClassRepository.save(
                new SpecificClass(false, "Studio C", LocalDate.of(2025, 11, 7), 18,
                        LocalTime.of(17, 30), LocalTime.of(18, 30), latinBasicsDance, null));

        // Create instructor Sarah
        Instructor sarah = instructorRepository.save(
            new Instructor("Sarah Connor", "Contemporary lead", "sarah@flow.com", "pass12345",
                    LocalDate.of(2025, 10, 1), false)
        );

        // Assign Sarah to beginnerBallet (so she has an active class)
        beginnerBallet.setInstructor(sarah);
        specificClassRepository.save(beginnerBallet);
    }

    @Test
    public void ST003_01_testAddNewInstructor_SuccessfulAssignment() throws Exception {
        String requestBody = String.format("""
            {
              "name": "Emma Lee",
              "bio": "Specialist in Latin",
              "email": "emma@flow.com",
              "password": "safePass123",
              "specificClassIds": [%d, %d],
              "isDeleted": false
            }
        """, beginnerBallet.getId(), latinBasics.getId());

        mockMvc.perform(post("/instructors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("emma@flow.com"))
                .andExpect(jsonPath("$.assignedSpecificClassIds.length()").value(2));
;
    }

    @Test
    public void ST003_02_testAddNewInstructor_FailNoSpecificClassSelected() throws Exception {
        String requestBody = """
            {
              "name": "Mark Chen",
              "bio": "Jazz improvisation",
              "email": "mark@flow.com",
              "password": "secretPass",
              "isDeleted": false
            }
        """;

        mockMvc.perform(post("/instructors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("At least one scheduled class (SpecificClass) must be assigned"));

    }

    @Test
    public void ST003_03_testAddNewInstructor_FailMissingRequiredDetails() throws Exception {
        String requestBody = String.format("""
            {
              "name": "",
              "bio": "Some bio",
              "email": "noemail",
              "password": "",
              "specificClassIds": [%d],
              "isDeleted": false
            }
        """, beginnerBallet.getId());

        mockMvc.perform(post("/instructors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", allOf(
                    hasItem("Name is required"),
                    hasItem("Enter a valid email address"),
                    hasItem("Password is required")
                )));
    }

    @Test
    public void ST003_04_testAddNewInstructor_FailDuplicateEmail() throws Exception {
        String requestBody = String.format("""
            {
              "name": "Sarah 2",
              "bio": "Another Sarah",
              "email": "sarah@flow.com",
              "password": "anyPass123",
              "specificClassIds": [%d],
              "isDeleted": false
            }
        """, latinBasics.getId());

        mockMvc.perform(post("/instructors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errors[0]").value("Email already in use by another person"));
    }

    // ✅ ST003_05
    @Test
    public void ST003_05_testAddNewInstructor_SuccessWithoutBio() throws Exception {
        String requestBody = String.format("""
            {
              "name": "Sophia Grant",
              "bio": "",
              "email": "sophia@flow.com",
              "password": "safePass123",
              "specificClassIds": [%d],
              "isDeleted": false
            }
        """, beginnerBallet.getId());

        mockMvc.perform(post("/instructors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bio").doesNotExist());
    }

    // ✅ ST003_06
    @Test
    public void ST003_06_testAddNewInstructor_FailIfIsDeletedTrue() throws Exception {
        String requestBody = String.format("""
            {
              "name": "Test Deleted",
              "bio": "Any bio",
              "email": "deleted@flow.com",
              "password": "pass123",
              "specificClassIds": [%d],
              "isDeleted": true
            }
        """, latinBasics.getId());

        mockMvc.perform(post("/instructors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("New instructors must not be created as deleted"));;
    }


    @Test
    public void ST004_01_testDeleteInstructorSuccess() throws Exception {
        // First, create an instructor with no assigned classes
        Instructor emma = instructorRepository.save(
                new Instructor("Emma Lee", "Latin specialist", "emma@flow.com",
                        "safePass123", LocalDate.of(2025, 10, 5), false)
        );

        mockMvc.perform(delete("/instructors/emma@flow.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("emma@flow.com")))
                .andExpect(jsonPath("$.message", is("Instructor removed successfully")));

        // Verify in DB that the instructor is marked deleted
        Instructor deletedInstructor = instructorRepository.findByEmail("emma@flow.com").get();
        assertTrue(deletedInstructor.getIsDeleted());
    }

    @Test
    public void ST004_02_testDeleteInstructorWithActiveClasses() throws Exception {
        // Sarah already has beginnerBallet assigned in setup
        mockMvc.perform(delete("/instructors/sarah@flow.com"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errors[0]", is("Cannot remove instructor with active classes")));

        // Verify instructor is still active
        Instructor sarah = instructorRepository.findByEmail("sarah@flow.com").get();
        assertFalse(sarah.getIsDeleted());
    }

    @Test
    public void ST004_03_testDeleteNonExistentInstructor() throws Exception {
        mockMvc.perform(delete("/instructors/nonexistent@flow.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]", is("Instructor not found")));
    }

    @Test
    public void ST004_04_testDeleteInstructor_InvalidEmailFormat() throws Exception {
        mockMvc.perform(delete("/instructors/invalid-email"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0]", is("Invalid email format")));
    }

    @Test
    public void ST004_05_testDeleteInstructor_EmptyEmail() throws Exception {
        mockMvc.perform(delete("/instructors/ "))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0]", is("email is required")));
    }

    @Test
    public void ST004_06_testDeleteInstructor_NullEmail() throws Exception {
    
        mockMvc.perform(delete("/instructors/null"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]", is("Invalid email format")));
    }

}
