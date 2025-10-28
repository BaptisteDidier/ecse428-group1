package com.ecse428.flowfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ecse428.flowfinder.model.Client;
import com.ecse428.flowfinder.service.ClientService;

@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "*")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<Client> registerClient(
        @RequestParam String name,
        @RequestParam(required = false) String bio,
        @RequestParam String email,
        @RequestParam String password
    ) {
        Client createdClient = clientService.createClient(name, bio, email, password);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }
}
