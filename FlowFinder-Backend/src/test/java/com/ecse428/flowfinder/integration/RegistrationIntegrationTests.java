package com.ecse428.flowfinder.integration;

public class RegistrationIntegrationTests {

    @Autowired
    private MockMvc MockMvc;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InstrusctorRepository instructorRepository;

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
