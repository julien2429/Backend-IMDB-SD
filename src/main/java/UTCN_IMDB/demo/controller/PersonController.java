package UTCN_IMDB.demo.controller;

import UTCN_IMDB.demo.model.Person;
import UTCN_IMDB.demo.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@CrossOrigin
public class PersonController {

    private final PersonService personService;

    @GetMapping("/person")
    public List<Person> getPersons() {
        return personService.getPersons();
    }

    @GetMapping("/person/firstName/{firstName}")
    public Person getPersonByFirstName(String firstName) {
        return personService.getPersonByFirstName(firstName);
    }

    @GetMapping("/person/lastName/{lastName}")
    public Person getPersonByLastName(String lastName) {
        return personService.getPersonByLastName(lastName);
    }

    @GetMapping("/person/birthDate/{birthDate}")
    public Person getPersonByBirthDate(String birthDate) {
        return personService.getPersonByBirthDate(birthDate);
    }

    @PostMapping("/person")
    public Person addPerson(Person person) {
        return personService.addPerson(person);
    }

    @PostMapping("/person/{uuid}")
    public Person updatePerson(UUID uuid, Person person) {
        return personService.updatePerson(uuid, person);
    }

    @DeleteMapping("/person/{uuid}")
    public void deletePerson(UUID uuid) {
        personService.deletePerson(uuid);
    }





}
