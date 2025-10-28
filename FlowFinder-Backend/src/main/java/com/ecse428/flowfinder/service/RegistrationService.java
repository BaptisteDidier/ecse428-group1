package com.ecse428.flowfinder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ecse428.flowfinder.model.Registration;
import com.ecse428.flowfinder.model.SpecificClass;
import com.ecse428.flowfinder.exception.FlowFinderException;
import com.ecse428.flowfinder.model.Client;
import com.ecse428.flowfinder.repository.ClientRepository;
import com.ecse428.flowfinder.repository.RegistrationRepository;
import com.ecse428.flowfinder.repository.SpecificClassRepository;

import jakarta.transaction.Transactional;

@Service
public class RegistrationService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private SpecificClassRepository specificClassRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Transactional
    public Registration createRegistration(Client client, SpecificClass specificClass) {
        if (client == null) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Client cannot be null");
        }
        if (specificClass == null) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Specific Class cannot be null");
        }

        List<Registration> listOfRegistrationsForSpecificClass = getRegistrationsByClass(specificClass);
        int numberOfRegistrations = listOfRegistrationsForSpecificClass.size();
        if (numberOfRegistrations >= specificClass.getLimit()) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "This class is full");
        }

        Registration.Key key = new Registration.Key(client, specificClass);

        if (registrationRepository.existsById(key)) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Client is already registered for this class");
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
    public List<Registration> getRegistrationsByClient(int clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(
                        () -> new FlowFinderException(HttpStatus.BAD_REQUEST, "Client not found with id: " + clientId));

        return registrationRepository.findByKey_Participant(client);
    }

    /**
     * Retrieves all registrations for a specific dance class.
     */
    public List<Registration> getRegistrationsByClass(SpecificClass specificClass) {
        if (specificClass == null) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "SpecificClass cannot be null");
        }

        return registrationRepository.findByKey_DanceClass(specificClass);
    }

    /**
     * Checks if a registration exists for the given client and class.
     */
    public boolean isRegistered(Client client, int specificClassId) {
        if (client == null) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Client cannot be null");
        }

        SpecificClass danceClass = specificClassRepository.findById(specificClassId)
                .orElseThrow(
                        () -> new FlowFinderException(HttpStatus.BAD_REQUEST,
                                "Class not found with id: " + specificClassId));

        Registration.Key key = new Registration.Key(client, danceClass);
        return registrationRepository.existsById(key);
    }

}
