package com.ecse428.flowfinder.repository;

import org.springframework.data.repository.CrudRepository;

import com.ecse428.flowfinder.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {
    
}
