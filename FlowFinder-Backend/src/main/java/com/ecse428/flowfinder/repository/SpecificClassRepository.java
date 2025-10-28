package com.ecse428.flowfinder.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.model.SpecificClass;

import java.time.LocalDate;
import java.util.Optional;

public interface SpecificClassRepository extends CrudRepository<SpecificClass, Integer> {
    SpecificClass findSpecificClassById(int id);
    Iterable<SpecificClass> findSpecificClassByInstructor(Instructor instructor);
    Iterable<SpecificClass> findSpecificClassByDanceClass(DanceClass danceClass);
    List<SpecificClass> findByIdIn(List<Long> ids);
    Iterable<SpecificClass> findSpecificClassByLocation(String location);
    Iterable<SpecificClass> findSpecificClassByDate(LocalDate date);
    Iterable<SpecificClass> findSpecificClassByDateBetween(LocalDate startDate, LocalDate endDate);
}
