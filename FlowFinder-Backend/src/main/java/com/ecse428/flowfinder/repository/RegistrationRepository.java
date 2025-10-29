package com.ecse428.flowfinder.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ecse428.flowfinder.model.Client;
import com.ecse428.flowfinder.model.Registration;
import com.ecse428.flowfinder.model.SpecificClass;

public interface RegistrationRepository extends CrudRepository<Registration, Registration.Key> {
    Registration findRegistrationByKey(Registration.Key key);

    List<Registration> findByKey_Participant(Client client);

    List<Registration> findByKey_DanceClass(SpecificClass specificClass);
}
