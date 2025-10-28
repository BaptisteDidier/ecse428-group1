package com.ecse428.flowfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ecse428.flowfinder.exception.FlowFinderException;
import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.repository.DanceClassRepository;

import jakarta.transaction.Transactional;

@Service
public class DanceClassService {

    @Autowired
    private DanceClassRepository danceClassRepository;

    @Transactional
    public DanceClass createDanceClass(boolean isPrivate, String name, String genre, String description) {
        validate(name, "Name");
        validate(genre, "Genre");
        validate(description, "Description");

        // optional uniqueness check by name
        for (DanceClass existing : danceClassRepository.findAll()) {
            if (existing.getName().equalsIgnoreCase(name)) {
                throw new FlowFinderException(HttpStatus.BAD_REQUEST,
                    String.format("A dance class with the name '%s' already exists.", name));
            }
        }

        DanceClass danceClass = new DanceClass(isPrivate, name, genre, description);
        return danceClassRepository.save(danceClass);
    }

    private void validate(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST,
                field + " cannot be null or empty");
        }
    }
    
    @Transactional
    public Iterable<DanceClass> getDanceClassesByGenre(String genre) {
        validate(genre, "Genre");
        return danceClassRepository.findByGenreIgnoreCase(genre);
    }
}