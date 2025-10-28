package com.ecse428.flowfinder.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ecse428.flowfinder.model.DanceClass;

@SpringBootTest
public class DanceClassRepositoryTests {

    @Autowired
    private DanceClassRepository danceClassRepository;

    @AfterEach
    public void clearDatabase() {
        danceClassRepository.deleteAll();
    }

    @Test
    public void testFindByGenreIgnoreCase_ExactMatch() {
        // Create test dance classes
        DanceClass ballet1 = new DanceClass(false, "Ballet Class 1", "Ballet", "Basic ballet class");
        DanceClass ballet2 = new DanceClass(false, "Ballet Class 2", "Ballet", "Advanced ballet class");
        DanceClass hiphop = new DanceClass(false, "Hip Hop Class", "Hip Hop", "Hip hop dance class");
        
        // Save to repository
        danceClassRepository.save(ballet1);
        danceClassRepository.save(ballet2);
        danceClassRepository.save(hiphop);

        // Test exact match
        Iterable<DanceClass> foundClasses = danceClassRepository.findByGenreIgnoreCase("Ballet");
        List<DanceClass> foundList = new ArrayList<>();
        foundClasses.forEach(foundList::add);

        assertEquals(2, foundList.size(), "Should find exactly 2 ballet classes");
        assertTrue(foundList.stream().allMatch(dc -> dc.getGenre().equalsIgnoreCase("Ballet")),
                "All found classes should have genre 'Ballet'");
    }

    @Test
    public void testFindByGenreIgnoreCase_CaseInsensitive() {
        // Create test dance class
        DanceClass ballet = new DanceClass(false, "Ballet Class", "Ballet", "Basic ballet class");
        danceClassRepository.save(ballet);

        // Test different case variations
        List<String> testCases = List.of("ballet", "BALLET", "Ballet", "bAlLeT");

        for (String testCase : testCases) {
            Iterable<DanceClass> foundClasses = danceClassRepository.findByGenreIgnoreCase(testCase);
            List<DanceClass> foundList = new ArrayList<>();
            foundClasses.forEach(foundList::add);

            assertEquals(1, foundList.size(),
                    String.format("Should find the ballet class when searching with '%s'", testCase));
            assertEquals("Ballet Class", foundList.get(0).getName(),
                    String.format("Should find 'Ballet Class' when searching with '%s'", testCase));
        }
    }

    @Test
    public void testFindByGenreIgnoreCase_NoMatch() {
        // Create test dance class
        DanceClass ballet = new DanceClass(false, "Ballet Class", "Ballet", "Basic ballet class");
        danceClassRepository.save(ballet);

        // Test with non-existing genre
        Iterable<DanceClass> foundClasses = danceClassRepository.findByGenreIgnoreCase("Salsa");
        List<DanceClass> foundList = new ArrayList<>();
        foundClasses.forEach(foundList::add);

        assertTrue(foundList.isEmpty(), "Should not find any classes for non-existing genre");
    }
}