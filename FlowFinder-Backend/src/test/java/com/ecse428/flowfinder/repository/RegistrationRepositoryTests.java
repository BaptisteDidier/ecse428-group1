package com.ecse428.flowfinder.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ecse428.flowfinder.model.Client;
import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.model.Registration;
import com.ecse428.flowfinder.model.SpecificClass;

@DataJpaTest
public class RegistrationRepositoryTests {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private SpecificClassRepository specificClassRepository;

    @Autowired
    private DanceClassRepository danceClassRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    private Client createClient() {
        Client c = new Client("Alice", "bio", "alice@example.com", "pw", LocalDate.now().minusYears(20), false);
        return clientRepository.save(c);
    }

    private Instructor createInstructor() {
        Instructor i = new Instructor("Bob", "bio", "bob@example.com", "pw", LocalDate.now().minusYears(30), false);
        return instructorRepository.save(i);
    }

    private DanceClass createDanceClass() {
        DanceClass d = new DanceClass(false, "Salsa 101", "Salsa", "Beginner salsa");
        return danceClassRepository.save(d);
    }

    private SpecificClass createSpecificClass(DanceClass d, Instructor inst) {
        SpecificClass s = new SpecificClass(false, "Studio A", LocalDate.now().plusDays(2), 10, LocalTime.of(10, 0), LocalTime.of(11, 0), d, inst);
        return specificClassRepository.save(s);
    }

    @Test
    public void testSaveAndFindRegistration() {
        Client c = createClient();
        DanceClass d = createDanceClass();
        Instructor i = createInstructor();
        SpecificClass sc = createSpecificClass(d, i);

        Registration.Key key = new Registration.Key(c, sc);
        Registration reg = new Registration(key);
        registrationRepository.save(reg);

        Registration found = registrationRepository.findRegistrationByKey(key);
        assertThat(found).isNotNull();
        assertThat(found.getKey().getParticipant().getId()).isEqualTo(c.getId());
        assertThat(found.getKey().getDanceClass().getId()).isEqualTo(sc.getId());
    }

    @Test
    public void testFindByClientAndSpecificClass() {
        Client c1 = createClient();
        Client c2 = new Client("Eve", "bio", "eve@example.com", "pw", LocalDate.now().minusYears(22), false);
        c2 = clientRepository.save(c2);

        DanceClass d = createDanceClass();
        Instructor i = createInstructor();
        SpecificClass sc1 = createSpecificClass(d, i);
        SpecificClass sc2 = createSpecificClass(d, i);

        registrationRepository.save(new Registration(new Registration.Key(c1, sc1)));
        registrationRepository.save(new Registration(new Registration.Key(c1, sc2)));
        registrationRepository.save(new Registration(new Registration.Key(c2, sc1)));

        List<Registration> byClient = registrationRepository.findByKey_Participant(c1);
        assertThat(byClient).hasSize(2);

        List<Registration> byClass = registrationRepository.findByKey_DanceClass(sc1);
        assertThat(byClass).hasSize(2);
    }

    @Test
    public void testExistsAndDeleteRegistration() {
        Client c = createClient();
        DanceClass d = createDanceClass();
        Instructor i = createInstructor();
        SpecificClass sc = createSpecificClass(d, i);

        Registration.Key key = new Registration.Key(c, sc);
        Registration reg = new Registration(key);
        registrationRepository.save(reg);

        boolean exists = registrationRepository.existsById(key);
        assertThat(exists).isTrue();

        registrationRepository.delete(reg);

        Registration after = registrationRepository.findRegistrationByKey(key);
        assertThat(after).isNull();
    }

}

