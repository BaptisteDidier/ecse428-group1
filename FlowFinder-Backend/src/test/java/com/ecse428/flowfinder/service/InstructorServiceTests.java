package com.ecse428.flowfinder.service;

import com.ecse428.flowfinder.dto.CreateInstructorRequest;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

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
        sc1.setStart(LocalTime.of(18, 0));
        sc1.setEnd(LocalTime.of(19, 0));
        sc1.setDate(LocalDate.of(2025, 11, 3));
     

        sc2 = new SpecificClass();
        sc2.setStart(LocalTime.of(19, 30));
        sc2.setEnd(LocalTime.of(20, 30));
        sc2.setDate(LocalDate.of(2025, 11, 5));
       
    }

    @Test
    void createInstructor_Success() {
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
    void createInstructor_DuplicateEmail() {
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
    void createInstructor_NoSpecificClasses() {
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
}
