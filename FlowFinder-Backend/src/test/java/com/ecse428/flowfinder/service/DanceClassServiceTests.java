package com.ecse428.flowfinder.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.ecse428.flowfinder.exception.FlowFinderException;
import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.repository.DanceClassRepository;

@ExtendWith(MockitoExtension.class)
public class DanceClassServiceTests {

    @Mock
    private DanceClassRepository danceClassRepository;

    @InjectMocks
    private DanceClassService danceClassService;

    @Test
    public void testGetDanceClassesByGenre_Success() {
        // Prepare test data
        DanceClass ballet1 = new DanceClass(false, "Ballet Class 1", "Ballet", "Basic ballet class");
        DanceClass ballet2 = new DanceClass(false, "Ballet Class 2", "Ballet", "Advanced ballet class");
        
        // Mock repository response
        when(danceClassRepository.findByGenreIgnoreCase("Ballet"))
            .thenReturn(Arrays.asList(ballet1, ballet2));

        // Test the service method
        Iterable<DanceClass> result = danceClassService.getDanceClassesByGenre("Ballet");
        
        // Verify results
        assertNotNull(result, "Result should not be null");
        assertEquals(2, ((Iterable<DanceClass>) result).spliterator().getExactSizeIfKnown(),
                "Should return exactly 2 ballet classes");
    }

    @Test
    public void testGetDanceClassesByGenre_EmptyResult() {
        // Mock repository response for non-existing genre
        when(danceClassRepository.findByGenreIgnoreCase("NonExisting"))
            .thenReturn(Collections.emptyList());

        // Test the service method
        Iterable<DanceClass> result = danceClassService.getDanceClassesByGenre("NonExisting");
        
        // Verify results
        assertNotNull(result, "Result should not be null");
        assertEquals(0, ((Iterable<DanceClass>) result).spliterator().getExactSizeIfKnown(),
                "Should return empty list for non-existing genre");
    }

    @Test
    public void testGetDanceClassesByGenre_NullGenre() {
        // Test with null genre
        FlowFinderException exception = assertThrows(FlowFinderException.class,
                () -> danceClassService.getDanceClassesByGenre(null),
                "Should throw FlowFinderException for null genre");
        
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Genre cannot be null or empty"));
    }

    @Test
    public void testGetDanceClassesByGenre_EmptyGenre() {
        // Test with empty genre
        FlowFinderException exception = assertThrows(FlowFinderException.class,
                () -> danceClassService.getDanceClassesByGenre("  "),
                "Should throw FlowFinderException for empty genre");
        
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Genre cannot be null or empty"));
    }
}
