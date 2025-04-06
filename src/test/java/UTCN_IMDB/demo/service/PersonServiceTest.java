package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.DTO.PersonDTO;
import UTCN_IMDB.demo.DTO.RoleDTO;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.model.Movie;
import UTCN_IMDB.demo.model.MovieCast;
import UTCN_IMDB.demo.model.Person;
import UTCN_IMDB.demo.model.Role;
import UTCN_IMDB.demo.repository.MovieCastRepository;
import UTCN_IMDB.demo.repository.MovieRepository;
import UTCN_IMDB.demo.repository.PersonRepository;
import UTCN_IMDB.demo.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private MovieCastRepository movieCastRepository;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private PersonService personService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPersons() {
        // given
        List<Person> people = List.of(new Person(), new Person());
        when(personRepository.findAll()).thenReturn(people);

        // when
        List<Person> result = personService.getPersons();

        // then
        assertEquals(2, result.size());
        assertEquals(people, result);
        verify(personRepository, times(1)).findAll();
    }

    @Test
    void testGetPersonByFirstName() throws CompileTimeException {
        // given
        String firstName = "John";
        Person person = new Person();
        person.setFirstName(firstName);
        when(personRepository.findByFirstName(firstName)).thenReturn(Optional.of(person));

        // when
        Person result = personService.getPersonByFirstName(firstName);

        // then
        assertEquals(firstName, result.getFirstName());
        verify(personRepository, times(1)).findByFirstName(firstName);
    }

    @Test
    void testGetPersonByFirstNameNotFound() {
        // given
        String firstName = "John";
        when(personRepository.findByFirstName(firstName)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompileTimeException.class, () -> personService.getPersonByFirstName(firstName));
        verify(personRepository, times(1)).findByFirstName(firstName);
    }

    @Test
    void testGetPersonByLastName() throws CompileTimeException {
        // given
        String lastName = "Doe";
        Person person = new Person();
        person.setLastName(lastName);
        when(personRepository.findByLastName(lastName)).thenReturn(Optional.of(person));

        // when
        Person result = personService.getPersonByLastName(lastName);

        // then
        assertEquals(lastName, result.getLastName());
        verify(personRepository, times(1)).findByLastName(lastName);
    }

    @Test
    void testGetPersonByLastNameNotFound() {
        // given
        String lastName = "Doe";
        when(personRepository.findByLastName(lastName)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompileTimeException.class, () -> personService.getPersonByLastName(lastName));
        verify(personRepository, times(1)).findByLastName(lastName);
    }

    @Test
    void testGetPersonByBirthDate() throws CompileTimeException, ParseException {
        // given
        String birthDateStr = "2000-01-01";
        Date birthDate = dateFormat.parse(birthDateStr);
        Person person = new Person();
        person.setBirthDate(birthDate);
        when(personRepository.findByBirthDate(birthDate)).thenReturn(Optional.of(person));

        // when
        Person result = personService.getPersonByBirthDate(birthDateStr);

        // then
        assertEquals(birthDate, result.getBirthDate());
        verify(personRepository, times(1)).findByBirthDate(birthDate);
    }

    @Test
    void testGetPersonByBirthDateNotFound() {
        // given
        String birthDateStr = "2000-01-01";
        Date birthDate = null;
        try {
            birthDate = dateFormat.parse(birthDateStr);
        } catch (ParseException e) {
            fail("Failed to parse date: " + e.getMessage());
        }
        when(personRepository.findByBirthDate(birthDate)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompileTimeException.class, () -> personService.getPersonByBirthDate(birthDateStr));
        verify(personRepository, times(1)).findByBirthDate(birthDate);
    }

    @Test
    void testAddPerson() throws CompileTimeException, ParseException {
        // given
        PersonDTO personDTO = new PersonDTO();
        personDTO.setFirstName("John");
        personDTO.setLastName("Doe");
        personDTO.setGender("Male");
        personDTO.setBirthDate(dateFormat.parse("2000-01-01")); // DTO still uses LocalDate
        personDTO.setNationality("US");
        //personDTO.setDeathDate(LocalDate.parse("2024-01-01"));

        Person personToSave = new Person();
        personToSave.setFirstName(personDTO.getFirstName());
        personToSave.setLastName(personDTO.getLastName());
        personToSave.setGender(personDTO.getGender());
        personToSave.setBirthDate(dateFormat.parse("2000-01-01"));
        personToSave.setNationality(personDTO.getNationality());
        //personToSave.setDeathDate(personDTO.getDeathDate() != null ? dateFormat.parse(personDTO.getDeathDate().toString()) : null);

        Person savedPerson = new Person();
        savedPerson.setPersonId(UUID.randomUUID());
        savedPerson.setFirstName(personToSave.getFirstName());
        savedPerson.setLastName(personToSave.getLastName());
        savedPerson.setGender(personToSave.getGender());
        savedPerson.setBirthDate(personToSave.getBirthDate());
        savedPerson.setNationality(personToSave.getNationality());
        savedPerson.setDeathDate(personToSave.getDeathDate());


        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        // when
        Person result = personService.addPerson(personDTO);

        // then
        assertNotNull(result.getPersonId());
        assertEquals(savedPerson.getFirstName(), result.getFirstName());
        assertEquals(savedPerson.getBirthDate(), result.getBirthDate());
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void testUpdatePerson() throws CompileTimeException, ParseException {
        // given
        UUID uuid = UUID.randomUUID();
        PersonDTO personDTO = new PersonDTO();
        personDTO.setFirstName("Jane");
        personDTO.setLastName("Smith");
        personDTO.setGender("Female");
        personDTO.setBirthDate(dateFormat.parse("1995-05-05")); // DTO still uses LocalDate
        personDTO.setNationality("UK");
        //personDTO.setDeathDate(LocalDate.parse("2025-05-05"));


        Person existingPerson = new Person();
        existingPerson.setPersonId(uuid);
        existingPerson.setFirstName("John");
        existingPerson.setLastName("Doe");
        existingPerson.setGender("Male");
        existingPerson.setBirthDate(dateFormat.parse("2000-01-01"));
        existingPerson.setNationality("US");
        //existingPerson.setDeathDate(dateFormat.parse("2024-01-01"));

        Person updatedPerson = new Person();
        updatedPerson.setPersonId(uuid);
        updatedPerson.setFirstName(personDTO.getFirstName());
        updatedPerson.setLastName(personDTO.getLastName());
        updatedPerson.setGender(personDTO.getGender());
        updatedPerson.setBirthDate(dateFormat.parse("1995-05-05"));
        updatedPerson.setNationality(personDTO.getNationality());
        //updatedPerson.setDeathDate(personDTO.getDeathDate() != null ? dateFormat.parse(personDTO.getDeathDate().toString()) : null);


        when(personRepository.findById(uuid)).thenReturn(Optional.of(existingPerson));
        when(personRepository.save(any(Person.class))).thenReturn(updatedPerson);

        // when
        Person result = personService.updatePerson(uuid, personDTO);

        // then
        assertEquals(personDTO.getFirstName(), result.getFirstName());
        assertEquals(personDTO.getLastName(), result.getLastName());
        assertEquals(personDTO.getGender(), result.getGender());
        assertEquals(dateFormat.parse("1995-05-05"), result.getBirthDate());
        assertEquals(personDTO.getNationality(), result.getNationality());
        verify(personRepository, times(1)).findById(uuid);
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void testUpdatePersonNotFound() {
        // given
        UUID uuid = UUID.randomUUID();
        PersonDTO personDTO = new PersonDTO();

        when(personRepository.findById(uuid)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompileTimeException.class, () -> personService.updatePerson(uuid, personDTO));
        verify(personRepository, times(1)).findById(uuid);
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    void testDeletePerson() {
        // given
        UUID uuid = UUID.randomUUID();
        doNothing().when(personRepository).deleteById(uuid);

        // when
        personService.deletePerson(uuid);

        // then
        verify(personRepository, times(1)).deleteById(uuid);
    }

    @Test
    void testGetPersonById() throws CompileTimeException, ParseException {
        // given
        UUID uuid = UUID.randomUUID();
        Date birthDate = dateFormat.parse("1990-03-15");
        Person person = new Person();
        person.setPersonId(uuid);
        person.setBirthDate(birthDate);
        when(personRepository.findById(uuid)).thenReturn(Optional.of(person));

        // when
        Person result = personService.getPersonById(uuid);

        // then
        assertEquals(uuid, result.getPersonId());
        assertEquals(birthDate, result.getBirthDate());
        verify(personRepository, times(1)).findById(uuid);
    }

    @Test
    void testGetPersonByIdNotFound() {
        // given
        UUID uuid = UUID.randomUUID();
        when(personRepository.findById(uuid)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompileTimeException.class, () -> personService.getPersonById(uuid));
        verify(personRepository, times(1)).findById(uuid);
    }

    @Test
    void testAddRole() throws CompileTimeException, ParseException {
        // given
        UUID personUUID = UUID.randomUUID();
        UUID movieUUID = UUID.randomUUID();
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName("Actor");

        Person person = new Person();
        person.setPersonId(personUUID);

        Movie movie = new Movie();
        movie.setMovieId(movieUUID);

        Role roleToSave = new Role();
        roleToSave.setRoleName(roleDTO.getRoleName());

        Role savedRole = new Role();
        savedRole.setRoleId(UUID.randomUUID());
        savedRole.setRoleName(roleToSave.getRoleName());

        MovieCast movieCastToSave = new MovieCast();
        movieCastToSave.setMovie(movie);
        movieCastToSave.setPerson(person);
        movieCastToSave.setRole(roleToSave);

        MovieCast savedMovieCast = new MovieCast();
        savedMovieCast.setCastId(UUID.randomUUID());
        savedMovieCast.setMovie(movie);
        savedMovieCast.setPerson(person);
        savedMovieCast.setRole(savedRole);


        when(personRepository.findById(personUUID)).thenReturn(Optional.of(person));
        when(movieRepository.findById(movieUUID)).thenReturn(Optional.of(movie));
        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);
        when(movieCastRepository.save(any(MovieCast.class))).thenReturn(savedMovieCast);

        // when
        MovieCast result = personService.addRole(personUUID, roleDTO, movieUUID);

        // then
        assertNotNull(result.getCastId());
        assertEquals(savedMovieCast.getRole().getRoleName(), result.getRole().getRoleName());
        verify(personRepository, times(1)).findById(personUUID);
        verify(movieRepository, times(1)).findById(movieUUID);
        verify(roleRepository, times(1)).save(any(Role.class));
        verify(movieCastRepository, times(1)).save(any(MovieCast.class));
    }

    @Test
    void testAddRolePersonNotFound() {
        // given
        UUID personUUID = UUID.randomUUID();
        UUID movieUUID = UUID.randomUUID();
        RoleDTO roleDTO = new RoleDTO();

        when(personRepository.findById(personUUID)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompileTimeException.class, () -> personService.addRole(personUUID, roleDTO, movieUUID));
        verify(personRepository, times(1)).findById(personUUID);
        verify(movieRepository, times(0)).findById(any());
        verify(roleRepository, times(0)).save(any());
        verify(movieCastRepository, times(0)).save(any());
    }

    @Test
    void testAddRoleMovieNotFound() throws CompileTimeException {
        // given
        UUID personUUID = UUID.randomUUID();
        UUID movieUUID = UUID.randomUUID();
        RoleDTO roleDTO = new RoleDTO();

        Person person = new Person();
        when(personRepository.findById(personUUID)).thenReturn(Optional.of(person));
        when(movieRepository.findById(movieUUID)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompileTimeException.class, () -> personService.addRole(personUUID, roleDTO, movieUUID));
        verify(personRepository, times(1)).findById(personUUID);
        verify(movieRepository, times(1)).findById(movieUUID);
        verify(roleRepository, times(0)).save(any());
        verify(movieCastRepository, times(0)).save(any());
    }
}