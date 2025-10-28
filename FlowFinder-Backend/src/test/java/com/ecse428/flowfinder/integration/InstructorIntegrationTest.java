package com.ecse428.flowfinder.integration;

import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.repository.InstructorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class InstructorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InstructorRepository instructorRepository;

    @BeforeEach
    public void setup() {
        instructorRepository.deleteAll();
    }

    /**
     * ST002_01 - Successful addition of a new instructor
     */
    @Test
    public void ST002_01_testAddInstructor_Successful() throws Exception {
        mockMvc.perform(post("/instructors")
                .param("name", "Deniz")
                .param("bio", "Specializes in hip hop dance")
                .param("email", "deniz@email.com")
                .param("password", "securePass123")
                .param("startDate", LocalDate.now().toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Deniz"))
                .andExpect(jsonPath("$.email").value("deniz@email.com"))
                .andExpect(jsonPath("$.bio").value("Specializes in hip hop dance"));

        // Verify in DB
        Instructor instructor = instructorRepository.findInstructorByEmail("deniz@email.com");
        assertThat(instructor).isNotNull();
        assertThat(instructor.getName()).isEqualTo("Deniz");
        assertThat(instructor.getEmail()).isEqualTo("deniz@email.com");
    }

    /**
     * ST002_02 - Addition with an email that is already in use
     */
    @Test
    public void ST002_02_testAddInstructor_DuplicateEmail() throws Exception {
        // Given an existing instructor in the DB
        Instructor existing = new Instructor(
                "Maria",
                "Experienced salsa instructor",
                "maria@email.com",
                "pass12345",
                LocalDate.now(),
                false
        );
        instructorRepository.save(existing);

        // When trying to add another instructor with the same email
        mockMvc.perform(post("/instructors")
                .param("name", "Maria Duplicate")
                .param("bio", "Duplicate email test")
                .param("email", "maria@email.com")
                .param("password", "anotherPass")
                .param("startDate", LocalDate.now().toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Email is already in use")));

        // Ensure no duplicate is created
        Iterable<Instructor> all = instructorRepository.findAll();
        assertThat(all).hasSize(1);
    }

    @Test
public void ST002_03_testAddInstructor_InvalidDetails() throws Exception {
    // Missing name and invalid email
    mockMvc.perform(post("/instructors")
            .param("name", "")  // empty name
            .param("bio", "Some bio")
            .param("email", "notanemail") // invalid email
            .param("password", "pass123")
            .param("startDate", LocalDate.now().toString())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Instructor name cannot be empty")));
    }
}
