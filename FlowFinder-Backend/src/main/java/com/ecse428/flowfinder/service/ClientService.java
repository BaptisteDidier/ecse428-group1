package com.ecse428.flowfinder.service;

import java.time.LocalDate;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ecse428.flowfinder.exception.FlowFinderException;
import com.ecse428.flowfinder.model.Client;
import com.ecse428.flowfinder.repository.ClientRepository;

import jakarta.transaction.Transactional;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    private final static LocalDate today = LocalDate.now();
    private final static String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    //private final static Client deletedClient = new Client("Deleted User", null, null, null, LocalDate.of(1970, 1, 1), true);
    
    @Transactional
    public Client createClient(String name, String bio, String email, String password) {
        validate(name, "Name");

        Client client = clientRepository.findClientByEmail(email);
        if (client != null) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, 
            String.format("%s is already taken.", email));
        }

        if (!Pattern.matches(emailRegex, email)) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, 
            String.format("Email must be in the form name@domain.tld"));
        }

        if (password.trim().length() < 8) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, 
            String.format("Password must be at least 8 characters"));
        }

        client = new Client(name, bio, email, password, today, false);
        return clientRepository.save(client);
    }


    // Helper function

    private void validate(String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, 
            String.format("%s cannot be null or empty", name));
        }
    }
}
