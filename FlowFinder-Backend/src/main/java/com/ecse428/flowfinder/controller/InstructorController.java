package com.ecse428.flowfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.ecse428.flowfinder.dto.CreateInstructorRequest;
import com.ecse428.flowfinder.dto.InstructorResponse;
import com.ecse428.flowfinder.service.InstructorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/instructors")
@CrossOrigin(origins = "*")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstructorResponse create(@Valid @RequestBody CreateInstructorRequest req) {
        return instructorService.createInstructor(req);
    }
}
