package UTCN_IMDB.demo.repository;

import UTCN_IMDB.demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
    Optional<Person> findByPersonId(UUID personId);
    Optional<Person> findByFirstName(String firstName);
    Optional<Person> findByLastName(String lastName);
    Optional<Person> findByBirthDate(Date birthDate);
    Optional<Person> findByGender(String gender);
}
