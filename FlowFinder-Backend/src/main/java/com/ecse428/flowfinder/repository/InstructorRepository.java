package com.ecse428.flowfinder.repository;

import org.springframework.data.repository.CrudRepository;

import com.ecse428.flowfinder.model.Instructor;

public interface InstructorRepository extends CrudRepository<Instructor, Integer> {
    public Instructor findInstructorById(int id);
    public boolean existsById(int id);
    public Instructor findInstructorByEmail(String email);
    public boolean existsByEmail(String email);
} 
