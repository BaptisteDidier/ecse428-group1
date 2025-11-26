package com.ecse428.flowfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ecse428.flowfinder.model.Client;
import com.ecse428.flowfinder.service.ClientService;
import com.ecse428.flowfinder.exception.FlowFinderException;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable int id) {
        try {
            clientService.deleteClient(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (FlowFinderException e) {
            return ResponseEntity
                    .status(e.getStatus())
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }
}
