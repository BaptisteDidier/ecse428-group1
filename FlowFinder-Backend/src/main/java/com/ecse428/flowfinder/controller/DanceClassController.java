package com.ecse428.flowfinder.controller;

import com.ecse428.flowfinder.dto.CreateDanceClassRequest;
import com.ecse428.flowfinder.dto.DanceClassResponse;
import com.ecse428.flowfinder.service.DanceClassService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/dance-classes")
public class DanceClassController {
    
    private final DanceClassService danceClassService;

    public DanceClassController(DanceClassService danceClassService) {
        this.danceClassService = danceClassService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DanceClassResponse create(@Valid @RequestBody CreateDanceClassRequest request) {
        return DanceClassResponse.from(danceClassService.createDanceClass(
            request.getIsPrivate(),
            request.getName(),
            request.getGenre(),
            request.getDescription()
        ));
    }
} 