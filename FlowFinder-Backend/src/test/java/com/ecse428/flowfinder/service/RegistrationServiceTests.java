package com.ecse428.flowfinder.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ecse428.flowfinder.exception.FlowFinderException;
import com.ecse428.flowfinder.model.Client;
import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.model.Registration;
import com.ecse428.flowfinder.model.Registration.Key;
import com.ecse428.flowfinder.model.SpecificClass;
import com.ecse428.flowfinder.repository.ClientRepository;
import com.ecse428.flowfinder.repository.RegistrationRepository;
import com.ecse428.flowfinder.repository.SpecificClassRepository;

public class RegistrationServiceTests {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private SpecificClassRepository specificClassRepository;

    @InjectMocks
    private RegistrationService registrationService;

    private Client client;
    private Instructor instructor;
    private DanceClass danceClass;
    private SpecificClass specificClass;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new Client("client1", "bio1", "client1@email.com",
                "password1",
                LocalDate.of(2024, Month.SEPTEMBER, 18), false);
        instructor = new Instructor("instructor1", "bio1", "instructor1@email.com",
                "password1",
                LocalDate.of(2024, Month.SEPTEMBER, 18), false);
        danceClass = new DanceClass(false, "danceClassName1", "danceGenre1", "dance class description 1");
        specificClass = new SpecificClass(false, "Studio A", LocalDate.now().plusDays(10), 12, LocalTime.of(10, 0),
                LocalTime.of(11, 0), danceClass, instructor);

        instructor.setIsDeleted(false);
    }

    @Test
    public void US012_01_testCreateRegistration_success() {
        Registration.Key key = new Key(client, specificClass);
        Registration savedRegistration = new Registration(key);

        // Mock the repository save method
        when(registrationRepository.save(any(Registration.class))).thenReturn(savedRegistration);

        Registration result = registrationService.createRegistration(client, specificClass);

        assertNotNull(result);
        assertEquals(client, result.getKey().getParticipant());
        assertEquals(specificClass, result.getKey().getDanceClass());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    public void US012_02_testCreateRegistration_nullClient_throwsFlowFinderException() {

        FlowFinderException exception = assertThrows(FlowFinderException.class, () -> {
            registrationService.createRegistration(null, specificClass);
        });

        assertEquals("Client cannot be null", exception.getMessage());
    }

    @Test
    public void US012_03_testCreateRegistration_ClassNotFound_throwsFlowFinderException() {
        FlowFinderException exception = assertThrows(FlowFinderException.class, () -> {
            registrationService.createRegistration(client, null);
        });

        assertEquals("Specific Class cannot be null", exception.getMessage());
    }

    @Test
    public void US012_04_testCreateRegistration_DuplicateRegistration_throwsFlowFinderException() {
        Registration.Key key = new Key(client, specificClass);
        Registration savedRegistration = new Registration(key);

        // Mock the repository save method
        when(registrationRepository.save(any(Registration.class))).thenReturn(savedRegistration);

        Registration result = registrationService.createRegistration(client, specificClass);

        assertNotNull(result);
        assertEquals(client, result.getKey().getParticipant());
        assertEquals(specificClass, result.getKey().getDanceClass());
        verify(registrationRepository, times(1)).save(any(Registration.class));

        when(registrationRepository.existsById(key)).thenReturn(true);

        FlowFinderException exception = assertThrows(FlowFinderException.class, () -> {
            registrationService.createRegistration(client, specificClass);
        });

        assertEquals("Client is already registered for this class", exception.getMessage());
    }

    @Test
    public void US012_05_testCreateRegistration_ClassFull_throwsFlowFinderException() {
        specificClass.setLimit(1);

        when(registrationRepository.findByKey_SpecificClass(specificClass))
                .thenReturn(List.of(new Registration(new Registration.Key(client, specificClass))));

        FlowFinderException exception = assertThrows(FlowFinderException.class, () -> {
            registrationService.createRegistration(client, specificClass);
        });

        assertEquals("This class is full", exception.getMessage());
        verify(registrationRepository, times(0)).save(any(Registration.class));
    }

}
