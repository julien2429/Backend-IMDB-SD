package UTCN_IMDB.demo.controller;

import UTCN_IMDB.demo.DTO.PersonDTO;
import UTCN_IMDB.demo.DTO.RoleDTO;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.model.Movie;
import UTCN_IMDB.demo.model.MovieCast;
import UTCN_IMDB.demo.model.Person;
import UTCN_IMDB.demo.model.Role;
import UTCN_IMDB.demo.service.PersonService;
import jakarta.validation.Valid;
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

    @GetMapping("/person/{uuid}")
    public Person getPersonById(@PathVariable UUID uuid) throws CompileTimeException{
        return personService.getPersonById(uuid);
    }

    @GetMapping("/person/firstName/{firstName}")
    public Person getPersonByFirstName(@PathVariable String firstName) throws CompileTimeException{
        return personService.getPersonByFirstName(firstName);
    }

    @GetMapping("/person/lastName/{lastName}")
    public Person getPersonByLastName(@PathVariable String lastName) throws CompileTimeException{
        return personService.getPersonByLastName(lastName);
    }

    @GetMapping("/person/birthDate/{birthDate}")
    public Person getPersonByBirthDate(@PathVariable String birthDate) throws CompileTimeException{
        return personService.getPersonByBirthDate(birthDate);
    }

    @PostMapping("/person")
    public Person addPerson(@RequestBody @Valid PersonDTO personDTO) throws CompileTimeException {
        return personService.addPerson(personDTO);
    }

    @PutMapping("/person/{uuid}")
    public Person updatePerson(@PathVariable UUID uuid, @RequestBody @Valid PersonDTO personDTO) throws CompileTimeException{
        return personService.updatePerson(uuid, personDTO);
    }

    @DeleteMapping("/person/{uuid}")
    public void deletePerson(@PathVariable UUID uuid) {
        personService.deletePerson(uuid);
    }

    @PutMapping("/person/addRole/{personUUID}/{movieUUID}")
    public MovieCast addRole(@PathVariable UUID personUUID, @PathVariable UUID movieUUID, @RequestBody RoleDTO role) throws CompileTimeException {
       return personService.addRole(personUUID, role, movieUUID);
    }

}
