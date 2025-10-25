package com.ecse428.flowfinder.repository;

import com.ecse428.flowfinder.model.Instructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends CrudRepository<Instructor, Integer> {
  Instructor findByEmail(String email);
  Instructor findByName(String name);
  boolean existsByEmail(String email);
}