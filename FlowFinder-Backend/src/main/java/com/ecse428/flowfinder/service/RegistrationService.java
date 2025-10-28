package com.ecse428.flowfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ecse428.flowfinder.model.Registration;
import com.ecse428.flowfinder.model.SpecificClass;
import com.ecse428.flowfinder.exception.FlowFinderException;
import com.ecse428.flowfinder.model.Client;
import com.ecse428.flowfinder.repository.ClientRepository;
import com.ecse428.flowfinder.repository.RegistrationRepository;
import com.ecse428.flowfinder.repository.SpecificClassRepository;

public class RegistrationService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private SpecificClassRepository specificClassRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    public Registration createRegistration(int clientId, int classId) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(
                        () -> new FlowFinderException(HttpStatus.BAD_REQUEST, "Client not found with id: " + clientId));

        SpecificClass danceClass = specificClassRepository.findById(classId)
                .orElseThrow(
                        () -> new FlowFinderException(HttpStatus.BAD_REQUEST, "Class not found with id: " + classId));

        Registration.Key key = new Registration.Key(client, danceClass);

        if (registrationRepository.existsById(key)) {
            new FlowFinderException(HttpStatus.BAD_REQUEST, "Client is already registered for this class.");
        }

        Registration registration = new Registration(key);
        return registrationRepository.save(registration);
    }

}
