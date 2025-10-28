package com.ecse428.flowfinder.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.ecse428.flowfinder.exception.FlowFinderException;
import com.ecse428.flowfinder.model.SpecificClass;
import com.ecse428.flowfinder.repository.SpecificClassRepository;
import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.model.Instructor;

public class SpecificClassServiceTests {

    // all tests are not explicitly related to any user story as none of the user
    // stories relate to Specific Class
    // However, these tests are indirectly linked to User Story 12 Scenario 1
    // (Normal flow) as a specific class is
    // needed to create a successful booking.

    @Mock
    private SpecificClassRepository specificClassRepository;

    @InjectMocks
    private SpecificClassService specificClassService;

    private Instructor instructor;
    private DanceClass danceClass;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        instructor = new Instructor("instructor1", "bio1", "instructor1@email.com",
                "password1",
                LocalDate.of(2024, Month.SEPTEMBER, 18), false);
        danceClass = new DanceClass(false, "danceClassName1", "danceGenre1", "dance class description 1");

        instructor.setIsDeleted(false);
    }

    @Test
    void testCreateSpecificClass_success() {
        String location = "Studio A";
        LocalDate classDate = LocalDate.now().plusDays(10);
        int limit = 10;
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        SpecificClass savedClass = new SpecificClass(false, location, classDate, limit, startTime, endTime,
                danceClass, instructor);

        // Mock the repository save method
        when(specificClassRepository.save(any(SpecificClass.class))).thenReturn(savedClass);

        SpecificClass result = specificClassService.createSpecificClass(location,
                classDate, limit, startTime, endTime, danceClass, instructor);

        assertNotNull(result);
        assertEquals(location, result.getLocation());
        assertEquals(classDate, result.getDate());
        assertEquals(limit, result.getLimit());
        assertEquals(startTime, result.getStart());
        assertEquals(endTime, result.getEnd());
        assertEquals(danceClass, result.getDanceClass());
        assertEquals(instructor, result.getInstructor());
        verify(specificClassRepository, times(1)).save(any(SpecificClass.class));
    }

    @Test
    public void testCreateSpecificClass_NullDanceClass_throwsFlowFinderException() {
        String location = "Studio A";
        LocalDate classDate = LocalDate.now().plusDays(10);
        int limit = 10;
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        FlowFinderException exception = assertThrows(FlowFinderException.class, () -> {
            specificClassService.createSpecificClass(location, classDate, limit, startTime, endTime, null, instructor);
        });

        assertEquals("Class or Instructor cannot be null", exception.getMessage());
    }

    @Test
    public void testCreateSpecificClass_NullInstructor_throwsFlowFinderException() {
        String location = "Studio A";
        LocalDate classDate = LocalDate.now().plusDays(10);
        int limit = 10;
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        FlowFinderException exception = assertThrows(FlowFinderException.class, () -> {
            specificClassService.createSpecificClass(location, classDate, limit, startTime, endTime, danceClass, null);
        });

        assertEquals("Class or Instructor cannot be null", exception.getMessage());
    }

    @Test
    void testCreateSpecificClass_NegativeLimit_throwsFlowFinderException() {
        String location = "Studio A";
        LocalDate classDate = LocalDate.now().plusDays(10);
        int limit = -10;
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        FlowFinderException exception = assertThrows(FlowFinderException.class, () -> {
            specificClassService.createSpecificClass(location, classDate, limit, startTime, endTime, danceClass,
                    instructor);
        });

        assertEquals("Class limit must be positive", exception.getMessage());
    }

    @Test
    void testCreateSpecificClass_PastDate_throwsFlowFinderException() {
        String location = "Studio A";
        LocalDate classDate = LocalDate.now().minusDays(10);
        int limit = 10;
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        FlowFinderException exception = assertThrows(FlowFinderException.class, () -> {
            specificClassService.createSpecificClass(location, classDate, limit, startTime, endTime, danceClass,
                    instructor);
        });

        assertEquals("Date of class cannot be in the past", exception.getMessage());
    }

    @Test
    void testCreateSpecificClass_PastTime_throwsFlowFinderException() {
        String location = "Studio A";
        LocalDate classDate = LocalDate.now();
        int limit = 10;
        LocalTime startTime = LocalTime.now().minusHours(1);
        LocalTime endTime = LocalTime.now().plusHours(2);

        FlowFinderException exception = assertThrows(FlowFinderException.class, () -> {
            specificClassService.createSpecificClass(location, classDate, limit, startTime, endTime, danceClass,
                    instructor);
        });

        assertEquals("Class start and end time must be after current time", exception.getMessage());
    }

    @Test
    void testCreateSpecificClass_emptyLocation_throwsFlowFinderException() {
        String location = "       ";
        LocalDate classDate = LocalDate.now().plusDays(10);
        int limit = 10;
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        FlowFinderException exception = assertThrows(FlowFinderException.class, () -> {
            specificClassService.createSpecificClass(location, classDate, limit, startTime, endTime, danceClass,
                    instructor);
        });

        assertEquals("Location cannot be empty", exception.getMessage());
    }

}
