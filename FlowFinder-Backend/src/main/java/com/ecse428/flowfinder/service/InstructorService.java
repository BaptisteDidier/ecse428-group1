package com.ecse428.flowfinder.service;

import com.ecse428.flowfinder.dto.CreateInstructorRequest;
import com.ecse428.flowfinder.dto.InstructorResponse;
import com.ecse428.flowfinder.exception.FlowFinderException;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.model.SpecificClass;
import com.ecse428.flowfinder.repository.InstructorRepository;
import com.ecse428.flowfinder.repository.PersonRepository;
import com.ecse428.flowfinder.repository.SpecificClassRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepo;
    private final PersonRepository personRepo;
    private final SpecificClassRepository specificClassRepo;

    public InstructorService(InstructorRepository instructorRepo,
                             PersonRepository personRepo,
                             SpecificClassRepository specificClassRepo) {
        this.instructorRepo = instructorRepo;
        this.personRepo = personRepo;
        this.specificClassRepo = specificClassRepo;
    }

    public InstructorResponse createInstructor(CreateInstructorRequest req) {
        // Uniqueness across Person table (single-table inheritance)
        // Prefer a case-insensitive exists method. If you only have existsByEmail(...),
        // keep it but consider adding existsByEmailIgnoreCase(...) in PersonRepository.
        boolean emailInUse = personRepo.existsByEmail(req.getEmail());

        if (emailInUse) {
            throw new FlowFinderException(HttpStatus.CONFLICT, "Email already in use by another person");
        }

        if (Boolean.TRUE.equals(req.getIsDeleted())) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "New instructors must not be created as deleted");
        }        

        if (req.getSpecificClassIds() == null || req.getSpecificClassIds().isEmpty()) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST,
                    "At least one scheduled class (SpecificClass) must be assigned");
        }

        // Create instructor
        Instructor inst = new Instructor();
        inst.setName(req.getName());
        inst.setBio(req.getBio());
        inst.setEmail(req.getEmail());
        inst.setPassword(req.getPassword());
        inst.setCreationDate(LocalDate.now());
        inst.setIsDeleted(false);

        // Persist to get an ID
        inst = instructorRepo.save(inst);

        // Assign to SpecificClass
        List<SpecificClass> classes = specificClassRepo.findByIdIn(req.getSpecificClassIds());
        if (classes.size() != req.getSpecificClassIds().size()) {
            throw new FlowFinderException(HttpStatus.NOT_FOUND, "One or more SpecificClass IDs are invalid");
        }
        for (SpecificClass sc : classes) {
            if (Boolean.TRUE.equals(sc.getIsDeleted())) {
                throw new FlowFinderException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "Cannot assign to a deleted SpecificClass id=" + sc.getId());
            }
            sc.setInstructor(inst);
        }
        specificClassRepo.saveAll(classes);

        return InstructorResponse.from(inst, classes);
    }
}