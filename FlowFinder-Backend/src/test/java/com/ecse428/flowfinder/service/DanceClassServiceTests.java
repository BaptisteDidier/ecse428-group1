package com.ecse428.flowfinder.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.ecse428.flowfinder.exception.FlowFinderException;
import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.repository.DanceClassRepository;

class DanceClassServiceTests {

    @Mock
    private DanceClassRepository danceClassRepository;

    @InjectMocks
    private DanceClassService danceClassService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this); // manual init (no MockitoExtension)
    }

    @Test
    void US005_01_createDanceClass_success_savesAndReturnsEntity() {
        // Arrange
        boolean isPrivate = false;
        String name = "Beginner Salsa";
        String genre = "Salsa";
        String description = "Intro class";

        // service checks duplicates via findAll() first
        when(danceClassRepository.findAll()).thenReturn(List.of());

        DanceClass saved = new DanceClass(isPrivate, name, genre, description);
        when(danceClassRepository.save(any(DanceClass.class))).thenReturn(saved);

        // Act
        DanceClass result = danceClassService.createDanceClass(isPrivate, name, genre, description);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(genre, result.getGenre());
        assertEquals(description, result.getDescription());

        // Verify interactions (both findAll and save)
        verify(danceClassRepository, times(1)).findAll();

        ArgumentCaptor<DanceClass> captor = ArgumentCaptor.forClass(DanceClass.class);
        verify(danceClassRepository, times(1)).save(captor.capture());
        DanceClass toSave = captor.getValue();
        assertEquals(isPrivate, toSave.getIsPrivate());
        assertEquals(name, toSave.getName());
        assertEquals(genre, toSave.getGenre());
        assertEquals(description, toSave.getDescription());

        verifyNoMoreInteractions(danceClassRepository);
    }

    @Test
    void US005_02_createDanceClass_duplicateName_throwsFlowFinderException() {
        // Arrange: existing class with same name (case-insensitive)
        DanceClass existing = new DanceClass(false, "Beginner Salsa", "Salsa", "Existing");
        when(danceClassRepository.findAll()).thenReturn(List.of(existing));

        // Act + Assert
        FlowFinderException ex = assertThrows(
                FlowFinderException.class,
                () -> danceClassService.createDanceClass(false, "BEGINNER SALSA", "Salsa", "New desc")
        );
        assertTrue(ex.getMessage().toLowerCase().contains("already exists"));

        verify(danceClassRepository, times(1)).findAll();
        verify(danceClassRepository, never()).save(any());
        verifyNoMoreInteractions(danceClassRepository);
    }

    @Test
    void US005_03_createDanceClass_blankName_throwsFlowFinderException() {
        FlowFinderException ex = assertThrows(
                FlowFinderException.class,
                () -> danceClassService.createDanceClass(true, "   ", "HipHop", "Basics")
        );
        assertTrue(ex.getMessage().contains("Name"));
        // validation fails before any repo call
        verifyNoInteractions(danceClassRepository);
    }

    @Test
    void US005_04_createDanceClass_nullGenre_throwsFlowFinderException() {
        FlowFinderException ex = assertThrows(
                FlowFinderException.class,
                () -> danceClassService.createDanceClass(false, "Tap 101", null, "Tap intro")
        );
        assertTrue(ex.getMessage().contains("Genre"));
        verifyNoInteractions(danceClassRepository);
    }

    @Test
    void US005_05_createDanceClass_blankDescription_throwsFlowFinderException() {
        FlowFinderException ex = assertThrows(
                FlowFinderException.class,
                () -> danceClassService.createDanceClass(false, "Ballet 1", "Ballet", "   ")
        );
        assertTrue(ex.getMessage().contains("Description"));
        verifyNoInteractions(danceClassRepository);
    }
}
