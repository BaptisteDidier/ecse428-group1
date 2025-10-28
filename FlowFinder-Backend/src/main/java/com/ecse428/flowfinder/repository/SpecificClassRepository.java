package com.ecse428.flowfinder.repository;

import org.springframework.data.repository.CrudRepository;

import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.model.SpecificClass;

import java.time.LocalDate;
import java.util.Optional;

public interface SpecificClassRepository extends CrudRepository<SpecificClass, Integer> {
    Optional<SpecificClass> findById(int id);

    Iterable<SpecificClass> findByInstructor(Instructor instructor);

    Iterable<SpecificClass> findByDanceClass(DanceClass danceClass);

    Iterable<SpecificClass> findByLocation(String location);

    Iterable<SpecificClass> findByDate(LocalDate date);

    Iterable<SpecificClass> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
