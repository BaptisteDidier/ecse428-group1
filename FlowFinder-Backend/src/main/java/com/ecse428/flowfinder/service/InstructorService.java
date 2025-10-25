package com.ecse428.flowfinder.service;

import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.repository.InstructorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepo;

    public InstructorService(InstructorRepository instructorRepo) {
        this.instructorRepo = instructorRepo;
    }

    
    public Instructor addNewInstructor(String name, String bio, String email, String password, LocalDate startDate) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Instructor name cannot be empty");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Instructor email cannot be empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Instructor password cannot be empty");
        }

        // Use the constructor from Instructor/Person
        Instructor instructor = new Instructor(name, bio, email, password, startDate, false);

        return instructorRepo.save(instructor);
    }
}
