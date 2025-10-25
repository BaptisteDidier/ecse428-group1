package com.ecse428.flowfinder.repository;

import org.springframework.data.repository.CrudRepository;

import com.ecse428.flowfinder.model.Registration;

public interface RegistrationRepository extends CrudRepository<Registration, Registration.Key> {
    Registration findRegistrationByKey(Registration.Key key);
    Iterable<Registration> findByKey_ParticipantId(int id);
}
