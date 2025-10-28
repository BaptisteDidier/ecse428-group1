package com.ecse428.flowfinder.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import com.ecse428.flowfinder.repository.ClientRepository;
import com.ecse428.flowfinder.repository.DanceClassRepository;
import com.ecse428.flowfinder.repository.InstructorRepository;
import com.ecse428.flowfinder.repository.RegistrationRepository;
import com.ecse428.flowfinder.repository.SpecificClassRepository;

public class RegistrationIntegrationTests {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private DanceClassRepository danceClassRepository;

    @Autowired
    private SpecificClassRepository specificClassRepository;

    @BeforeEach
    public void setup() {
        registrationRepository.deleteAll();
        clientRepository.deleteAll();
        instructorRepository.deleteAll();
        danceClassRepository.deleteAll();
        specificClassRepository.deleteAll();
    }

}
