package com.ecse428.flowfinder.controller;

import com.ecse428.flowfinder.dto.CreateDanceClassRequest;
import com.ecse428.flowfinder.dto.DanceClassResponse;
import com.ecse428.flowfinder.service.DanceClassService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    
    @GetMapping("/by-genre/{genre}")
    public List<DanceClassResponse> getDanceClassesByGenre(@PathVariable String genre) {
        return StreamSupport.stream(danceClassService.getDanceClassesByGenre(genre).spliterator(), false)
            .map(DanceClassResponse::from)
            .collect(Collectors.toList());
    }
} 