package com.ecse428.flowfinder.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ecse428.flowfinder.model.Instructor;

@SpringBootTest
public class InstructorRepositoryTests {

    // Inject the repository we are testing
    @Autowired
    private InstructorRepository instructorRepository;

    @AfterEach
    public void clearDatabase() {
        instructorRepository.deleteAll();
    }
    private Instructor createTestInstructor(String email, String name) {
        return new Instructor(
                name,
                "Test Bio",
                email,
                "Password123",
                LocalDate.now(),
                false
        );
    }

    @Test
    public void testFindInstructorById_Success() {
        // Arrange
        Instructor instructor = createTestInstructor("john.doe@example.com", "John Doe");
        instructorRepository.save(instructor);
        int instructorId = instructor.getId();

        // Act
        Instructor foundInstructor = instructorRepository.findInstructorById(instructorId);

        // Assert
        assertNotNull(foundInstructor, "Instructor should be found by ID");
        assertEquals(instructor.getName(), foundInstructor.getName(), "Found instructor name should match");
    }

    @Test
    public void testFindInstructorById_NotFound() {
        // Act
        Instructor foundInstructor = instructorRepository.findInstructorById(9999); // Use a non-existent ID

        // Assert
        assertNull(foundInstructor, "No instructor should be found for non-existent ID");
    }

    @Test
    public void testFindInstructorByEmail_Success() {
        // Arrange
        String testEmail = "test.email@university.com";
        Instructor instructor = createTestInstructor(testEmail, "Alice Smith");
        instructorRepository.save(instructor);

        // Act
        Instructor foundInstructor = instructorRepository.findInstructorByEmail(testEmail);

        // Assert
        assertNotNull(foundInstructor, "Instructor should be found by email");
        assertEquals(testEmail, foundInstructor.getEmail(), "Found instructor email should match the search email");
    }

    @Test
    public void testFindInstructorByEmail_NoMatch() {
        // Arrange
        Instructor instructor = createTestInstructor("unique@example.com", "Bob");
        instructorRepository.save(instructor);

        // Act
        Instructor foundInstructor = instructorRepository.findInstructorByEmail("nonexistent@university.edu");

        // Assert
        assertNull(foundInstructor, "No instructor should be found for a non-existent email");
    }

    @Test
    public void testExistsById_True() {
        // Arrange
        Instructor instructor = createTestInstructor("exists@example.com", "Exists User");
        instructorRepository.save(instructor);
        int instructorId = instructor.getId();

        // Act
        boolean exists = instructorRepository.existsById(instructorId);

        // Assert
        assertTrue(exists, "existsById should return true for a saved instructor ID");
    }

    @Test
    public void testExistsById_False() {
        // Act
        boolean exists = instructorRepository.existsById(9998);

        // Assert
        assertFalse(exists, "existsById should return false for a non-existent ID");
    }

    @Test
    public void testExistsByEmail_True() {
        // Arrange
        String testEmail = "exist.email@school.com";
        Instructor instructor = createTestInstructor(testEmail, "Tester");
        instructorRepository.save(instructor);

        // Act
        boolean exists = instructorRepository.existsByEmail(testEmail);

        // Assert
        assertTrue(exists, "existsByEmail should return true for a saved instructor email");
    }

    @Test
    public void testExistsByEmail_False() {
        // Act
        boolean exists = instructorRepository.existsByEmail("idontexist@school.com");

        // Assert
        assertFalse(exists, "existsByEmail should return false for a non-existent email");
    }
}