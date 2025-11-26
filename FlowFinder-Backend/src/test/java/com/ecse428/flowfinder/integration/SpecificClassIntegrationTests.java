package com.ecse428.flowfinder.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.ecse428.flowfinder.exception.FlowFinderException;
import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.model.SpecificClass;
import com.ecse428.flowfinder.repository.DanceClassRepository;
import com.ecse428.flowfinder.repository.InstructorRepository;
import com.ecse428.flowfinder.repository.SpecificClassRepository;
import com.ecse428.flowfinder.service.SpecificClassService;

@SpringBootTest
public class SpecificClassIntegrationTests {

    @Autowired
    private SpecificClassService specificClassService;

    @Autowired
    private SpecificClassRepository specificClassRepository;

    @Autowired
    private DanceClassRepository danceClassRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @AfterEach
    public void clearDatabase() {
        specificClassRepository.deleteAll();
        danceClassRepository.deleteAll();
        instructorRepository.deleteAll();
    }

    private DanceClass createDanceClass() {
        DanceClass dc = new DanceClass(false, "Salsa 101", "Salsa", "Basic Salsa techniques");
        return danceClassRepository.save(dc);
    }

    private Instructor createActiveInstructor() {
        Instructor instructor = new Instructor(
                "Natalia",
                "Salsa instructor",
                "natalia@example.com",
                "password123",
                LocalDate.now().minusDays(10), // hire/creation date, arbitrary
                false                          // not deleted
        );
        return instructorRepository.save(instructor);
    }

    private Instructor createDeletedInstructor() {
        Instructor instructor = new Instructor(
                "Old Instructor",
                "Former instructor",
                "deleted@example.com",
                "password123",
                LocalDate.now().minusDays(30),
                true                           // deleted
        );
        
        return instructorRepository.save(instructor);
    }

    // ST007 – Normal flow: successfully delete an existing SpecificClass
    @Test
    public void ST007_01_deleteSpecificClass_success_whenExists() {
        DanceClass danceClass = createDanceClass();
        Instructor instructor = createActiveInstructor();

        LocalDate date = LocalDate.now().plusDays(3);
        LocalTime start = LocalTime.of(18, 0);
        LocalTime end = LocalTime.of(19, 0);

        SpecificClass sc = specificClassService.createSpecificClass(
                "Studio A",
                date,
                10,
                start,
                end,
                danceClass,
                instructor
        );

        int id = sc.getId();
        assertTrue(specificClassRepository.findById(id).isPresent(),
                "SpecificClass should exist before deletion");

        specificClassService.deleteSpecificClass(id);

        assertFalse(specificClassRepository.findById(id).isPresent(),
                "SpecificClass should be removed from the repository after deletion");
    }

    // ST007 – Error flow: trying to delete a non-existent SpecificClass
    @Test
    public void ST007_02_deleteSpecificClass_nonExisting_throwsNotFound() {
        int nonExistingId = 9999;

        FlowFinderException ex = assertThrows(
                FlowFinderException.class,
                () -> specificClassService.deleteSpecificClass(nonExistingId),
                "Deleting a non-existent SpecificClass should throw FlowFinderException"
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus(),
                "Status should be 400 BAD_REQUEST for non-existent SpecificClass");
        assertTrue(ex.getMessage().contains("No Specific class found"),
                "Error message should indicate that no SpecificClass was found for the given ID");
    }

    // ST007 – Validation: date in the past should be rejected
    @Test
    public void ST007_03_createSpecificClass_pastDate_throwsValidationError() {
        DanceClass danceClass = createDanceClass();
        Instructor instructor = createActiveInstructor();

        LocalDate pastDate = LocalDate.now().minusDays(1);
        LocalTime start = LocalTime.of(18, 0);
        LocalTime end = LocalTime.of(19, 0);

        FlowFinderException ex = assertThrows(
                FlowFinderException.class,
                () -> specificClassService.createSpecificClass(
                        "Studio B",
                        pastDate,
                        10,
                        start,
                        end,
                        danceClass,
                        instructor
                ),
                "Creating a SpecificClass with a past date should throw FlowFinderException"
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus(),
                "Status should be 400 BAD_REQUEST for invalid date");
        assertTrue(ex.getMessage().contains("in the past"),
                "Error message should indicate that the date cannot be in the past");
    }

    // ST007 – Validation: instructor marked as deleted should be rejected
    @Test
    public void ST007_04_createSpecificClass_deletedInstructor_throwsValidationError() {
        DanceClass danceClass = createDanceClass();
        Instructor deletedInstructor = createDeletedInstructor();

        LocalDate date = LocalDate.now().plusDays(2);
        LocalTime start = LocalTime.of(18, 0);
        LocalTime end = LocalTime.of(19, 0);

        FlowFinderException ex = assertThrows(
                FlowFinderException.class,
                () -> specificClassService.createSpecificClass(
                        "Studio C",
                        date,
                        10,
                        start,
                        end,
                        danceClass,
                        deletedInstructor
                ),
                "Creating a SpecificClass with a deleted instructor should throw FlowFinderException"
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus(),
                "Status should be 400 BAD_REQUEST when instructor is deleted");
        assertTrue(ex.getMessage().contains("Instructor account is deleted"),
                "Error message should indicate that the instructor account is deleted");
    }

    // ST007 – Validation: end time must be after start time
    @Test
    public void ST007_05_createSpecificClass_invalidTimeRange_throwsValidationError() {
        DanceClass danceClass = createDanceClass();
        Instructor instructor = createActiveInstructor();

        LocalDate date = LocalDate.now().plusDays(2);
        LocalTime start = LocalTime.of(18, 0);
        LocalTime end = LocalTime.of(17, 0); // end before start

        FlowFinderException ex = assertThrows(
                FlowFinderException.class,
                () -> specificClassService.createSpecificClass(
                        "Studio D",
                        date,
                        10,
                        start,
                        end,
                        danceClass,
                        instructor
                ),
                "Creating a SpecificClass with end time before start time should throw FlowFinderException"
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus(),
                "Status should be 400 BAD_REQUEST for invalid time range");
        assertTrue(ex.getMessage().contains("start and end time"),
                "Error message should indicate an issue with start/end time");
    }
}
