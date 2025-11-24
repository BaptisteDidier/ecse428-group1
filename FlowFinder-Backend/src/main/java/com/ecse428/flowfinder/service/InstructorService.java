package com.ecse428.flowfinder.service;

import com.ecse428.flowfinder.dto.CreateInstructorRequest;
import com.ecse428.flowfinder.dto.DeleteInstructorResponse;
import com.ecse428.flowfinder.dto.InstructorResponse;
import com.ecse428.flowfinder.exception.FlowFinderException;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.model.SpecificClass;
import com.ecse428.flowfinder.repository.InstructorRepository;
import com.ecse428.flowfinder.repository.PersonRepository;
import com.ecse428.flowfinder.repository.SpecificClassRepository;

import java.util.Optional;
import java.util.regex.Pattern;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepo;
    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private SpecificClassRepository specificClassRepo;

    @Transactional
    public InstructorResponse createInstructor(CreateInstructorRequest req) {
        // Uniqueness across Person table (single-table inheritance)
        // Prefer a case-insensitive exists method. If you only have existsByEmail(...),
        // keep it but consider adding existsByEmailIgnoreCase(...) in PersonRepository.
        boolean emailInUse = personRepo.existsByEmail(req.getEmail());
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        if (req.getName() == null || req.getName().trim().isEmpty()) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Name is required");
        }

        if (req.getPassword() == null || req.getPassword().isEmpty()) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Password is required");
        }

        if (req.getEmail() == null || !VALID_EMAIL_ADDRESS_REGEX.matcher(req.getEmail()).matches()) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Enter a valid email address");
        }

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
    @Transactional
    public DeleteInstructorResponse deleteInstructorByEmail(String email) {
        Optional<Instructor> instructorOpt = instructorRepo.findByEmail(email);

        if (instructorOpt.isEmpty()) {
            throw new FlowFinderException(HttpStatus.NOT_FOUND, "Instructor not found");
        }

        Instructor instructor = instructorOpt.get();

        // Check for active classes
        boolean hasActiveClasses = specificClassRepo.existsByInstructorEmailAndIsDeletedFalse(email);
        if (hasActiveClasses) {
            throw new FlowFinderException(HttpStatus.CONFLICT, "Cannot remove instructor with active classes");
        }

        instructor.setIsDeleted(true);
        instructorRepo.save(instructor);

        return new DeleteInstructorResponse(email, "Instructor removed successfully");
    }

}