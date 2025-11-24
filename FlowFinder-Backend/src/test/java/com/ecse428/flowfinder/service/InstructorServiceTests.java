package com.ecse428.flowfinder.service;

import com.ecse428.flowfinder.dto.CreateInstructorRequest;
import com.ecse428.flowfinder.dto.DeleteInstructorResponse;
import com.ecse428.flowfinder.dto.InstructorResponse;
import com.ecse428.flowfinder.exception.FlowFinderException;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.model.SpecificClass;
import com.ecse428.flowfinder.repository.InstructorRepository;
import com.ecse428.flowfinder.repository.PersonRepository;
import com.ecse428.flowfinder.repository.SpecificClassRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class InstructorServiceTests {

    @Mock
    private InstructorRepository instructorRepo;

    @Mock
    private PersonRepository personRepo;

    @Mock
    private SpecificClassRepository specificClassRepo;

    @InjectMocks
    private InstructorService instructorService;

    private Instructor instructor;
    private SpecificClass sc1, sc2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        instructor = new Instructor();
        instructor.setName("Jane Doe");
        instructor.setEmail("jane@example.com");
        instructor.setPassword("pass123");
        instructor.setCreationDate(LocalDate.of(2025, 1, 1));
        instructor.setIsDeleted(false);

        sc1 = new SpecificClass();
        sc1.setStartTime(LocalTime.of(18, 0));
        sc1.setEndTime(LocalTime.of(19, 0));
        sc1.setDate(LocalDate.of(2025, 11, 3));

        sc2 = new SpecificClass();
        sc2.setStartTime(LocalTime.of(19, 30));
        sc2.setEndTime(LocalTime.of(20, 30));
        sc2.setDate(LocalDate.of(2025, 11, 5));

    }

    @Test
    void ST003_01_createInstructor_Success() {
        CreateInstructorRequest req = new CreateInstructorRequest();
        req.setName("Jane Doe");
        req.setEmail("jane@example.com");
        req.setPassword("pass123");
        req.setSpecificClassIds(Arrays.asList(1L, 2L));

        when(personRepo.existsByEmail("jane@example.com")).thenReturn(false);
        when(instructorRepo.save(any(Instructor.class))).thenReturn(instructor);
        when(specificClassRepo.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(sc1, sc2));

        InstructorResponse response  = instructorService.createInstructor(req);

        assertNotNull(response);
        assertEquals("Jane Doe", response.getName());
        assertEquals(2, response.getAssignedSpecificClassIds().size());
        verify(instructorRepo, times(1)).save(any(Instructor.class));
        verify(specificClassRepo, times(1)).saveAll(anyList());
    }


    @Test
    void ST003_02_createInstructor_NoSpecificClasses() {
        CreateInstructorRequest req = new CreateInstructorRequest();
        req.setName("Jane Doe");
        req.setEmail("jane@example.com");
        req.setPassword("pass123");
        req.setSpecificClassIds(List.of());

        when(personRepo.existsByEmail("jane@example.com")).thenReturn(false);

        FlowFinderException ex = assertThrows(FlowFinderException.class, () -> instructorService.createInstructor(req));
        assertEquals("At least one scheduled class (SpecificClass) must be assigned", ex.getMessage());
        verify(instructorRepo, never()).save(any());
    }

    @Test
    void ST003_03_createInstructor_MissingName() {
        CreateInstructorRequest req = new CreateInstructorRequest();
        req.setName(null); // Missing Name (or use "  " for empty string)
        req.setEmail("valid@email.com"); // Valid
        req.setPassword("pass123");      // Valid
        req.setSpecificClassIds(List.of(1L)); // Valid

        // Act & Assert
        FlowFinderException ex = assertThrows(FlowFinderException.class,
                () -> instructorService.createInstructor(req));

        // Assert: Throws the specific error message for Name
        assertEquals("Name is required", ex.getMessage());

    }

    @Test
    void ST003_03_createInstructor_MissingPassword() {
        CreateInstructorRequest req = new CreateInstructorRequest();
        req.setName("Valid Name");      // Valid
        req.setEmail("valid2@email.com"); // Valid
        req.setPassword("");            // Missing Password (Empty string)
        req.setSpecificClassIds(List.of(1L)); // Valid

        // Act & Assert
        FlowFinderException ex = assertThrows(FlowFinderException.class,
                () -> instructorService.createInstructor(req));

        // Assert: Throws the specific error message for Password
        assertEquals("Password is required", ex.getMessage());
    }

    @Test
    void ST003_03_createInstructor_InvalidEmailFormat() {
        CreateInstructorRequest req = new CreateInstructorRequest();
        req.setName("Valid Name");      // Valid
        req.setEmail("invalid-email");  // Invalid Email (Missing @ and domain)
        req.setPassword("pass123");     // Valid
        req.setSpecificClassIds(List.of(1L)); // Valid

        // Arrange: Mock the existence check, though the validation should stop before it runs
        when(personRepo.existsByEmail("invalid-email")).thenReturn(false);

        // Act & Assert
        FlowFinderException ex = assertThrows(FlowFinderException.class,
                () -> instructorService.createInstructor(req));

        // Assert
        assertEquals("Enter a valid email address", ex.getMessage());

        // Verify: No persistence occurred
        verify(instructorRepo, never()).save(any());
    }

    @Test
    void ST003_04_createInstructor_DuplicateEmail() {
        CreateInstructorRequest req = new CreateInstructorRequest();
        req.setName("Jane Doe");
        req.setEmail("jane@example.com");
        req.setPassword("pass123");
        req.setSpecificClassIds(List.of(1L));

        when(personRepo.existsByEmail("jane@example.com")).thenReturn(true);

        FlowFinderException ex = assertThrows(FlowFinderException.class, () -> instructorService.createInstructor(req));
        assertEquals("Email already in use by another person", ex.getMessage());
        verify(instructorRepo, never()).save(any());
    }

    @Test
    void ST004_01_RemoveInstructor_NoAssignedClasses() {
        Instructor emma = new Instructor("Emma Lee", "Latin specialist", "emma@flow.com",
                "safePass", LocalDate.of(2025, 10, 5), false);

        when(instructorRepo.findByEmail("emma@flow.com")).thenReturn(Optional.of(emma));
        when(specificClassRepo.existsByInstructorEmailAndIsDeletedFalse("emma@flow.com")).thenReturn(false);

        DeleteInstructorResponse response = instructorService.deleteInstructorByEmail("emma@flow.com");

        // Assert
        assertEquals("emma@flow.com", response.getEmail());
        assertEquals("Instructor removed successfully", response.getMessage());
    }

    @Test
    void ST004_02_RemoveInstructor_WithActiveClasses() {
        //Arrange
        Instructor sarah = new Instructor("Sarah Connor", "Contemporary lead",
                "sarah@flow.com", "pass12345", LocalDate.of(2025, 10, 1), false);
        when(instructorRepo.findByEmail("sarah@flow.com")).thenReturn(Optional.of(sarah));

        when(specificClassRepo.existsByInstructorEmailAndIsDeletedFalse("sarah@flow.com")).thenReturn(true);
        //Act
        FlowFinderException ex = assertThrows(FlowFinderException.class,
                () -> instructorService.deleteInstructorByEmail("sarah@flow.com"));

        //Assert
        assertEquals("Cannot remove instructor with active classes", ex.getMessage());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());

        verify(instructorRepo, never()).save(any());
    }

    @Test
    void ST004_03_RemoveInstructor_NonExistent() {
        //Act
        when(instructorRepo.findByEmail("nonexistent@flow.com")).thenReturn(Optional.empty());

        FlowFinderException ex = assertThrows(FlowFinderException.class,
                () -> instructorService.deleteInstructorByEmail("nonexistent@flow.com"));
        //Assert
        assertEquals("Instructor not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());

        verify(instructorRepo, never()).save(any());
    }

    @Test
    void ST004_04_RemoveInstructor_InvalidEmailFormat() {
    
        FlowFinderException ex = assertThrows(
                FlowFinderException.class,
                () -> instructorService.deleteInstructorByEmail("invalid-email")
        );
    
        assertEquals("Invalid email format", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    
        verify(instructorRepo, never()).findByEmail(any());
    }

    @Test
    void ST004_05_RemoveInstructor_EmptyEmail() {
    
        FlowFinderException ex = assertThrows(
                FlowFinderException.class,
                () -> instructorService.deleteInstructorByEmail("  ")
        );
    
        assertEquals("email is required", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    
        verify(instructorRepo, never()).findByEmail(any());
    }

    @Test
    void ST004_06_RemoveInstructor_NullEmail() {
    
        FlowFinderException ex = assertThrows(
                FlowFinderException.class,
                () -> instructorService.deleteInstructorByEmail(null)
        );
    
        assertEquals("email is required", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    
        verify(instructorRepo, never()).findByEmail(any());
    }
           
}
