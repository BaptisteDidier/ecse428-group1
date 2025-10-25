package com.ecse428.flowfinder.repository;

import org.springframework.data.repository.CrudRepository;

import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.model.SpecificClass;

public interface SpecificClassRepository extends CrudRepository<SpecificClass, Integer> {
    SpecificClass findSpecificClassById(int id);
    Iterable<SpecificClass> findSpecificClassByInstructor(Instructor instructor);
    Iterable<SpecificClass> findSpecificClassByDanceClass(DanceClass danceClass);
}
