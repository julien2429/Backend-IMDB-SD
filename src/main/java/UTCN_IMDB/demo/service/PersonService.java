package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.DTO.MovieRoleDTO;
import UTCN_IMDB.demo.DTO.PersonDTO;
import UTCN_IMDB.demo.DTO.RoleDTO;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.model.*;
import UTCN_IMDB.demo.repository.MovieCastRepository;
import UTCN_IMDB.demo.repository.MovieRepository;
import UTCN_IMDB.demo.repository.PersonRepository;
import UTCN_IMDB.demo.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PersonService {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final PersonRepository personRepository;
    private final MovieRepository movieRepository;
    private final MovieCastRepository movieCastRepository;
    private final RoleRepository roleRepository;

    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    public Person getPersonByFirstName(String firstName) throws CompileTimeException{
        return personRepository.findByFirstName(firstName).orElseThrow(
                () -> new CompileTimeException("Person with firstName " + firstName + " not found"));
    }

    public Person getPersonByLastName(String lastName) throws CompileTimeException{
        return personRepository.findByLastName(lastName).orElseThrow(
                () -> new CompileTimeException("Person with lastName " + lastName + " not found"));
    }

    public Person getPersonByBirthDate(String birthDate) throws CompileTimeException{
        dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        Date localDate;
        try {
            localDate = dateFormat.parse(birthDate);
        }
        catch (Exception e) {
            throw new CompileTimeException("Invalid date format. Expected format: yyyy-MM-dd");
        }



        return personRepository.findByBirthDate(localDate).orElseThrow(
                () -> new CompileTimeException("Person with birthDate " + birthDate + " not found"));
    }

    public Person addPerson(PersonDTO personDTO) throws CompileTimeException {

        Person person = new Person();
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setGender(personDTO.getGender());
        person.setBirthDate(personDTO.getBirthDate());
        person.setNationality(personDTO.getNationality());
        person.setDeathDate(personDTO.getDeathDate());
        return personRepository.save(person);
    }

    public Person updatePerson(UUID uuid, PersonDTO personDTO) throws CompileTimeException{
        Person existingPerson =
                personRepository.findById(uuid).orElseThrow(
                        () -> new CompileTimeException("Person with uuid " + uuid + " not found"));

        existingPerson.setFirstName(personDTO.getFirstName());
        existingPerson.setLastName(personDTO.getLastName());
        existingPerson.setBirthDate(personDTO.getBirthDate());
        existingPerson.setGender(personDTO.getGender());
        existingPerson.setNationality(personDTO.getNationality());
        existingPerson.setPersonId(uuid);

        return personRepository.save(existingPerson);
    }

    public void deletePerson(UUID uuid) {
        personRepository.deleteById(uuid);
    }

    public Person getPersonById(UUID uuid) throws CompileTimeException {
        return personRepository.findById(uuid).orElseThrow(
                () -> new CompileTimeException("Person with id " + uuid + " not found"));
    }

    public MovieCast addRole(UUID personUUID, RoleDTO role, UUID movieUUID) throws CompileTimeException {
        Person person = personRepository.findById(personUUID).orElseThrow(
                () -> new CompileTimeException("Person with id " + personUUID + " not found"));

        /// Role part
        Role newRole = new Role();
        newRole.setRoleName(role.getRoleName());

        //Check if movie exists
        Movie existingMovie = movieRepository.findById(movieUUID).orElseThrow(
                () -> new CompileTimeException("Movie with id " + movieUUID + " not found"));


        newRole = roleRepository.save(newRole);


        MovieCast movieCast = new MovieCast();
        movieCast.setMovie(existingMovie);
        movieCast.setPerson(person);
        movieCast.setRole(newRole);

        return movieCastRepository.save(movieCast);

    }

    public ActorDetails getActorDetails(UUID uuid) throws CompileTimeException {
        Person person = personRepository.findById(uuid).orElseThrow(
                () -> new CompileTimeException("Person with id " + uuid + " not found"));

        ActorDetails actorDetails = new ActorDetails();
        actorDetails.setFirstName(person.getFirstName());
        actorDetails.setLastName(person.getLastName());
        actorDetails.setGender(person.getGender());
        actorDetails.setBirthDate(person.getBirthDate());
        actorDetails.setNationality(person.getNationality());
        actorDetails.setDeathDate(person.getDeathDate());

        actorDetails.setMovieRoles(
                person.getMovieCastList().stream()
                        .map(movieCast -> {
                            MovieRoleDTO movieRoleDTO = new MovieRoleDTO();
                            movieRoleDTO.setMovieId(movieCast.getMovie().getMovieId());
                            movieRoleDTO.setMovieName(movieCast.getMovie().getTitle());
                            movieRoleDTO.setRoleName(movieCast.getRole().getRoleName());
                            return movieRoleDTO;
                        })
                        .toList()
        );

        return actorDetails;
    }




}
