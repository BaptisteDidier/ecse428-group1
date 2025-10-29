package com.ecse428.flowfinder.service;

import com.ecse428.flowfinder.exception.FlowFinderException;
import com.ecse428.flowfinder.model.Client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;


import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.ecse428.flowfinder.repository.ClientRepository;

@SpringBootTest
public class ClientServiceTests {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private static final String validName = "name";
    private static final String validBio = "Hi everyone!";
    private static final String validEmail = "email@gmail.com";
    private static final String validPassword = "12345678";
    private static final LocalDate validDate = LocalDate.now();

    private static final String emptyString = "";
    private static final String invalidEmail = "email";

    @Test
    public void US001_01_testRegisterClient_Successful() {
        when(clientRepository.save(any(Client.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));

        Client client = clientService.createClient(validName, validBio, validEmail, validPassword);

        assertNotNull(client);
        assertEquals(validName, client.getName());
        assertEquals(validBio, client.getBio());
        assertEquals(validEmail, client.getEmail());
        assertEquals(validPassword, client.getPassword());
        assertEquals(validDate, client.getCreationDate());
        assertFalse(client.getIsDeleted());

        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void US001_02_testRegisterClient_duplicateEmail() {
        when(clientRepository.findClientByEmail(validEmail)).thenReturn(new Client(validName, validBio, validEmail, validPassword, validDate, false));
        FlowFinderException exception = assertThrows(FlowFinderException.class, 
            () -> clientService.createClient(validName, validBio, validEmail, validPassword));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(String.format("%s is already taken.", validEmail), exception.getMessage());

        verify(clientRepository, never()).save(any(Client.class));
        verify(clientRepository, times(1)).findClientByEmail(validEmail);
    }

    @Test
    public void US001_03_testRegisterClient_missingName() {
        FlowFinderException exception = assertThrows(FlowFinderException.class, 
        () -> clientService.createClient(emptyString, validBio, validEmail, validPassword));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Name cannot be null or empty", exception.getMessage());

        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    public void US001_03_testRegisterClient_invalidEmail() {
        FlowFinderException exception = assertThrows(FlowFinderException.class, 
        () -> clientService.createClient(validName, validBio, invalidEmail, validPassword));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email must be in the form name@domain.tld", exception.getMessage());

        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    public void US001_04_testRegisterClient_emptyBio() {
        when(clientRepository.save(any(Client.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));

        Client client = clientService.createClient(validName, emptyString, validEmail, validPassword);

        assertNotNull(client);
        assertEquals(validName, client.getName());
        assertEquals(emptyString, client.getBio());
        assertEquals(validEmail, client.getEmail());
        assertEquals(validPassword, client.getPassword());
        assertEquals(validDate, client.getCreationDate());
        assertFalse(client.getIsDeleted());

        verify(clientRepository, times(1)).save(client);
    }
    
    @Test
public void US001_05_testGetClientById_Success() {
    
    Client mockClient = new Client(validName, validBio, validEmail, validPassword, validDate, false);
    
    when(clientRepository.findById(1)).thenReturn(Optional.of(mockClient));

    Client client = clientService.getClientById(1);

    assertNotNull(client);
    assertEquals(validName, client.getName());
    assertEquals(validBio, client.getBio());
    assertEquals(validEmail, client.getEmail());

    verify(clientRepository, times(1)).findById(1);
}

@Test
public void US001_06_testGetClientById_NotFound() {
    when(clientRepository.findById(999)).thenReturn(Optional.empty());

    FlowFinderException exception = assertThrows(FlowFinderException.class,
            () -> clientService.getClientById(999));

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("No client found with ID 999", exception.getMessage());

    verify(clientRepository, times(1)).findById(999);
}
}
