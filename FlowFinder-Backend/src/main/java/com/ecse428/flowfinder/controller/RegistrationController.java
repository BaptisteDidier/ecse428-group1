package com.ecse428.flowfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.ecse428.flowfinder.model.Client;
import com.ecse428.flowfinder.model.Registration;
import com.ecse428.flowfinder.model.SpecificClass;
import com.ecse428.flowfinder.repository.ClientRepository;
import com.ecse428.flowfinder.repository.SpecificClassRepository;
import com.ecse428.flowfinder.service.RegistrationService;
import com.ecse428.flowfinder.service.dto.CancellationOutcome;

@RestController
@RequestMapping("/registrations")
@CrossOrigin(origins = "*")
public class RegistrationController {

    @Autowired private RegistrationService registrationService;
    @Autowired private ClientRepository clientRepository;
    @Autowired private SpecificClassRepository specificClassRepository;

    @PostMapping
    public ResponseEntity<Registration> register(
        @RequestParam int clientId,
        @RequestParam int specificClassId) {

        Client client = clientRepository.findById(clientId).orElse(null);
        SpecificClass sc = specificClassRepository.findById(specificClassId).orElse(null);
        Registration reg = registrationService.createRegistration(client, sc);
        return new ResponseEntity<>(reg, HttpStatus.CREATED);
    }

    // Client cancels their booking
    @DeleteMapping
    public ResponseEntity<CancellationOutcome> cancel(
        @RequestParam int clientId,
        @RequestParam int specificClassId) {

        CancellationOutcome outcome = registrationService.cancelRegistration(clientId, specificClassId);
        return ResponseEntity.ok(outcome);
    }
}
