package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.model.Person;
import UTCN_IMDB.demo.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    public Person getPersonByFirstName(String firstName) {
        return personRepository.findByFirstName(firstName).orElseThrow(
                () -> new IllegalStateException("Person with firstName " + firstName + " not found"));
    }

    public Person getPersonByLastName(String lastName) {
        return personRepository.findByLastName(lastName).orElseThrow(
                () -> new IllegalStateException("Person with lastName " + lastName + " not found"));
    }

    public Person getPersonByBirthDate(String birthDate) {
        return personRepository.findByBirthDate(LocalDate.parse(birthDate)).orElseThrow(
                () -> new IllegalStateException("Person with birthDate " + birthDate + " not found"));
    }

    public Person addPerson(Person person) {
        return personRepository.save(person);
    }

    public Person updatePerson(UUID uuid, Person person) {
        Person existingPerson =
                personRepository.findById(uuid).orElseThrow(
                        () -> new IllegalStateException("Person with uuid " + uuid + " not found"));
        existingPerson.setFirstName(person.getFirstName());
        existingPerson.setLastName(person.getLastName());
        existingPerson.setBirthDate(person.getBirthDate());
        existingPerson.setGender(person.getGender());
        existingPerson.setPersonId(person.getPersonId());

        return personRepository.save(existingPerson);
    }

    public void deletePerson(UUID uuid) {
        personRepository.deleteById(uuid);
    }

    public Person getPersonById(UUID uuid) {
        return personRepository.findById(uuid).orElseThrow(
                () -> new IllegalStateException("Person with id " + uuid + " not found"));
    }




}
