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

    @Autowired
    private ClientService clientService;

    @Autowired
    private SpecificClassService specificClassService;

    @PostMapping
    public ResponseEntity<?> createRegistration(
        @RequestParam(required = false) Integer clientId,
        @RequestParam(required = false) Integer specificClassId
    ) {
        try {
            if (clientId == null) {
                return new ResponseEntity<>("Client cannot be null", HttpStatus.BAD_REQUEST);
            }
            if (specificClassId == null) {
                return new ResponseEntity<>("Specific Class cannot be null", HttpStatus.BAD_REQUEST);
            }

            Client client = clientService.getClientById(clientId);
            SpecificClass specificClass = specificClassService.getSpecificClassById(specificClassId);

            Registration registration = registrationService.createRegistration(client, specificClass);
            return new ResponseEntity<>(registration, HttpStatus.CREATED);

        } catch (FlowFinderException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRegistration(
            @RequestParam int clientId,
            @RequestParam int specificClassId
    ) {
        try {
            registrationService.deleteRegistration(clientId, specificClassId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (FlowFinderException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }
}
