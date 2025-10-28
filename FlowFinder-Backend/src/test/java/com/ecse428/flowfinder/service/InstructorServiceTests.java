package com.ecse428.flowfinder.service;

import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.repository.InstructorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InstructorServiceTests {

    @Mock
    private InstructorRepository instructorRepo;

    @InjectMocks
    private InstructorService instructorService;

    private LocalDate startDate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startDate = LocalDate.of(2025, 1, 1); // fixed date for consistency
    }

    @Test
    void addNewInstructor_Success() {
        Instructor mockInstructor = new Instructor("Jane Doe", "Bio", "jane@example.com", "pass123", startDate, false);

        when(instructorRepo.save(any(Instructor.class))).thenReturn(mockInstructor);

        Instructor result = instructorService.addNewInstructor("Jane Doe", "Bio", "jane@example.com", "pass123", startDate);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane@example.com", result.getEmail());
        verify(instructorRepo, times(1)).save(any(Instructor.class));
    }

    @Test
    void addNewInstructor_NameEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                instructorService.addNewInstructor("", "Bio", "jane@example.com", "pass123", startDate)
        );
        assertEquals("Instructor name cannot be empty", exception.getMessage());
        verify(instructorRepo, never()).save(any());
    }

    @Test
    void testAddNewInstructor_EmailEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                instructorService.addNewInstructor("Jane Doe", "Bio", "", "pass123", startDate)
        );
        assertEquals("Instructor email cannot be empty", exception.getMessage());
        verify(instructorRepo, never()).save(any());
    }

    @Test
    void testAddNewInstructor_PasswordEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                instructorService.addNewInstructor("Jane Doe", "Bio", "jane@example.com", "", startDate)
        );
        assertEquals("Instructor password cannot be empty", exception.getMessage());
        verify(instructorRepo, never()).save(any());
    }


}