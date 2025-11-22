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

    @Transactional
    public DanceClass deleteDanceClass(String name) {
        validate(name, "Name");

        // Find class by name (case-insensitive)
        DanceClass danceClass = null;
        for (DanceClass dc : danceClassRepository.findAll()) {
            if (dc.getName().equalsIgnoreCase(name)) {
                danceClass = dc;
                break;
            }
        }

        if (danceClass == null) {
            throw new FlowFinderException(HttpStatus.NOT_FOUND,
                String.format("Dance class with name '%s' does not exist.", name));
        }

        //Check if any SpecificClass exists for this DanceClass
        Iterable<SpecificClass> specificClasses = 
            specificClassService.getSpecificClassesByDanceClass(danceClass);

        if (specificClasses.iterator().hasNext()) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST,
                String.format("Cannot delete '%s' because specific classes exist for this dance class.",
                            danceClass.getName()));
        }

        danceClassRepository.delete(danceClass);
        return danceClass;
    }

    
}
