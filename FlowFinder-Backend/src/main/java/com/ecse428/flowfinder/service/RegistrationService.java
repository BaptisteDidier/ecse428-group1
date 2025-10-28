package com.ecse428.flowfinder.service;

import java.util.List;

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

    public Registration createRegistration(int clientId, int specificClassId) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(
                        () -> new FlowFinderException(HttpStatus.BAD_REQUEST,
                                "Client not found with id: " + specificClassId));

        SpecificClass danceClass = specificClassRepository.findById(specificClassId)
                .orElseThrow(
                        () -> new FlowFinderException(HttpStatus.BAD_REQUEST,
                                "Class not found with id: " + specificClassId));

        Registration.Key key = new Registration.Key(client, danceClass);

        if (registrationRepository.existsById(key)) {
            new FlowFinderException(HttpStatus.BAD_REQUEST, "Client is already registered for this class.");
        }

        Registration registration = new Registration(key);
        return registrationRepository.save(registration);
    }

    public void deleteRegistration(int clientId, int specificClassId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(
                        () -> new FlowFinderException(HttpStatus.BAD_REQUEST, "Client not found with id: " + clientId));

        SpecificClass danceClass = specificClassRepository.findById(specificClassId)
                .orElseThrow(
                        () -> new FlowFinderException(HttpStatus.BAD_REQUEST,
                                "Class not found with id: " + specificClassId));

        Registration.Key key = new Registration.Key(client, danceClass);

        if (!registrationRepository.existsById(key)) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Registration does not exist.");
        }

        registrationRepository.deleteById(key);
    }

    /**
     * Retrieves all registrations.
     */
    public Iterable<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    /**
     * Retrieves all registrations for a specific client.
     */
    public Iterable<Registration> getRegistrationsByClient(int clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(
                        () -> new FlowFinderException(HttpStatus.BAD_REQUEST, "Client not found with id: " + clientId));

        return registrationRepository.findByKey_ParticipantId(clientId);
    }

    /**
     * Retrieves all registrations for a specific dance class.
     */
    public List<Registration> getRegistrationsByClass(int specificClassId) {
        SpecificClass danceClass = specificClassRepository.findById(specificClassId)
                .orElseThrow(
                        () -> new FlowFinderException(HttpStatus.BAD_REQUEST,
                                "Class not found with id: " + specificClassId));

        return registrationRepository.findByKey_SpecificClassId(specificClassId);
    }

    /**
     * Checks if a registration exists for the given client and class.
     */
    public boolean isRegistered(int clientId, int specificClassId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(
                        () -> new FlowFinderException(HttpStatus.BAD_REQUEST, "Client not found with id: " + clientId));

        SpecificClass danceClass = specificClassRepository.findById(specificClassId)
                .orElseThrow(
                        () -> new FlowFinderException(HttpStatus.BAD_REQUEST,
                                "Class not found with id: " + specificClassId));

        Registration.Key key = new Registration.Key(client, danceClass);
        return registrationRepository.existsById(key);
    }

}
