package com.ecse428.flowfinder.repository;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ecse428.flowfinder.model.Client;
import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.service.SpecificClassService;

public class SpecificClassRepositoryTests {

        @InjectMocks
        private SpecificClassService specificClassService;

        @Mock
        private SpecificClassRepository specificClassRepository;

        @Mock
        private ClientRepository clientRepository;

        @Mock
        private InstructorRepository instructorRepository;

        @Mock
        private DanceClassRepository danceClassRepository;

        private Client client1;
        private Client client2;
        private Instructor instructor1;
        private Instructor instructor2;
        private DanceClass danceClass1;
        private DanceClass danceClass2;

        @BeforeEach
        public void setUp() {
                client1 = new Client("client1", "bio1", "client1@email.com", "password1",
                                LocalDate.of(2024, Month.SEPTEMBER, 18), false);
                client2 = new Client("client2", "bio2", "client2@email.com", "password2",
                                LocalDate.of(2024, Month.SEPTEMBER, 18), false);
                instructor1 = new Instructor("instructor1", "bio1", "instructor1@email.com",
                                "password1",
                                LocalDate.of(2024, Month.SEPTEMBER, 18), false);
                instructor2 = new Instructor("instructor2", "bio2", "instructor2@email.com",
                                "password2",
                                LocalDate.of(2024, Month.SEPTEMBER, 18), false);
                danceClass1 = new DanceClass(false, "danceClassName1", "danceGenre1", "dance class description 1");
                danceClass1 = new DanceClass(false, "danceClassName2", "danceGenre2", "dance class description 2");
        }

}
