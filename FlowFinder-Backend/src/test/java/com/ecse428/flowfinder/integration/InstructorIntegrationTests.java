package com.ecse428.flowfinder.integration;

import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.model.SpecificClass;
import com.ecse428.flowfinder.repository.DanceClassRepository;
import com.ecse428.flowfinder.repository.InstructorRepository;
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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.allOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class InstructorIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private SpecificClassRepository specificClassRepository;

    @Autowired
    private DanceClassRepository danceClassRepository;

    private SpecificClass beginnerBallet;
    private SpecificClass advancedHipHop;
    private SpecificClass latinBasics;

    @BeforeEach
    public void setup() {
        instructorRepository.deleteAll();
        specificClassRepository.deleteAll();
        danceClassRepository.deleteAll();

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

       instructorRepository.save(
        new Instructor("Sarah Connor", "Contemporary lead", "sarah@flow.com", "pass12345",
                   LocalDate.of(2025, 10, 1), false)
        );
    }

    @Test
    public void ST003_01_testAddNewInstructor_SuccessfulAssignment() throws Exception {
        mockMvc.perform(post("/instructors")
                .param("name", "Emma Lee")
                .param("bio", "Specialist in Latin")
                .param("email", "emma@flow.com")
                .param("password", "safePass123")
                .param("specificClassIds", String.valueOf(beginnerBallet.getId()),
                                           String.valueOf(latinBasics.getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("emma@flow.com"))
                .andExpect(jsonPath("$.specificClasses.length()").value(2));
    }

    @Test
    public void ST003_02_testAddNewInstructor_FailNoSpecificClassSelected() throws Exception {
        mockMvc.perform(post("/instructors")
                .param("name", "Mark Chen")
                .param("bio", "Jazz improvisation")
                .param("email", "mark@flow.com")
                .param("password", "secretPass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("At least one scheduled class (SpecificClass) must be assigned"));
    }

    @Test
    public void ST003_03_testAddNewInstructor_FailMissingRequiredDetails() throws Exception {
        mockMvc.perform(post("/instructors")
                .param("name", "")
                .param("bio", "Some bio")
                .param("email", "noemail")
                .param("password", "")
                .param("specificClassIds", String.valueOf(beginnerBallet.getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(allOf(
                        containsString("Name is required"),
                        containsString("Enter a valid email address"),
                        containsString("Password is required")
                )));
    }

    @Test
    public void ST003_04_testAddNewInstructor_FailDuplicateEmail() throws Exception {
        mockMvc.perform(post("/instructors")
                .param("name", "Sarah 2")
                .param("bio", "Another Sarah")
                .param("email", "sarah@flow.com")
                .param("password", "anyPass123")
                .param("specificClassIds", String.valueOf(latinBasics.getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already in use by another person"));
    }

    @Test
    public void ST003_05_testAddNewInstructor_SuccessWithoutBio() throws Exception {
        mockMvc.perform(post("/instructors")
                .param("name", "Sophia Grant")
                .param("bio", "")
                .param("email", "sophia@flow.com")
                .param("password", "safePass123")
                .param("specificClassIds", String.valueOf(beginnerBallet.getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bio").doesNotExist());
    }

    @Test
    public void ST003_06_testAddNewInstructor_FailIfIsDeletedTrue() throws Exception {
        mockMvc.perform(post("/instructors")
                .param("name", "Test Deleted")
                .param("bio", "Any bio")
                .param("email", "deleted@flow.com")
                .param("password", "pass")
                .param("isDeleted", "true")
                .param("specificClassIds", String.valueOf(latinBasics.getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("New instructors must not be created as deleted"));
    }
}
