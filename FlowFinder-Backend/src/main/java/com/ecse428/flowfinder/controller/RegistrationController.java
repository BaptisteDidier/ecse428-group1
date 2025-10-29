package com.ecse428.flowfinder.controller;

import com.ecse428.flowfinder.model.Client;
import com.ecse428.flowfinder.model.Registration;
import com.ecse428.flowfinder.model.SpecificClass;
import com.ecse428.flowfinder.service.ClientService;
import com.ecse428.flowfinder.service.RegistrationService;
import com.ecse428.flowfinder.service.SpecificClassService;
import com.ecse428.flowfinder.exception.FlowFinderException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registrations")
@CrossOrigin(origins = "*")
public class RegistrationController {
    
    @Autowired
    private RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<Registration> createRegistration(
            @RequestParam Client client,
            @RequestParam SpecificClass specificClass
    ) {
        Registration registration = registrationService.createRegistration(client, specificClass);
        return new ResponseEntity<>(registration, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteRegistration(
            @RequestParam int clientId,
            @RequestParam int specificClassId
    ) {
        registrationService.deleteRegistration(clientId, specificClassId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
