package com.ecse428.flowfinder.controller;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.service.InstructorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    /**
     * Add a new instructor
     * Example POST request parameters:
     *   name, bio, email, password, startDate (yyyy-MM-dd)
     */
    @PostMapping
    public ResponseEntity<?> addInstructor(
            @RequestParam String name,
            @RequestParam(required = false) String bio,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String startDate
    ) {
        try {

            LocalDate parsedDate = startDate == null || startDate.isEmpty() ? LocalDate.now() : LocalDate.parse(startDate);

            Instructor instructor = instructorService.addNewInstructor(name, bio, email, password, parsedDate);

            return new ResponseEntity<>(instructor, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
        
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
