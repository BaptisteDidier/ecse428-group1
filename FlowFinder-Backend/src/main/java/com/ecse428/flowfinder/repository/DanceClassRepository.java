package com.ecse428.flowfinder.repository;

import org.springframework.data.repository.CrudRepository;

import com.ecse428.flowfinder.model.DanceClass;

public interface DanceClassRepository extends CrudRepository<DanceClass, Integer> {
    DanceClass findDanceClassById(int id);
}
