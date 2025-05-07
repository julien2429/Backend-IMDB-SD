package UTCN_IMDB.demo.controller;

import UTCN_IMDB.demo.DTO.PersonDTO;
import UTCN_IMDB.demo.DTO.RoleDTO;
import UTCN_IMDB.demo.config.BCryptHashing;
import UTCN_IMDB.demo.enums.UserRole;
import UTCN_IMDB.demo.model.*;
import UTCN_IMDB.demo.repository.MovieRepository;
import UTCN_IMDB.demo.repository.PersonRepository;
import UTCN_IMDB.demo.repository.RoleRepository;
import UTCN_IMDB.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class PersonControllerIntegrationTest {

    /// BE SURE TO ADD ENV VARIABLES
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Person person1;
    private Person person2;
    private Movie movie1;
    private User admin;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String token;

    @BeforeEach
    @Transactional
    void setUp() throws Exception {
        personRepository.deleteAll();
        movieRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();

        objectMapper.registerModule(new JavaTimeModule());
        dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        seedDatabase();
        createAndLoginAdminUser();
    }

    @Transactional
    protected void createAndLoginAdminUser() throws Exception {

        admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@admi.com");
        String hashedPassword = BCryptHashing.hashPassword("admin");
        admin.setPassword(hashedPassword);
        admin.setRole(UserRole.ADMIN);
        userRepository.save(admin);

        MvcResult result = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "username": "admin",
                        "password": "admin"
                    }
                """))
            .andExpect(status().isOk())
                .andReturn();;

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        this.token = jsonNode.get("token").asText();
    }

    private void seedDatabase() throws Exception {
        person1 = new Person();
        person1.setFirstName("Keanu");
        person1.setLastName("Reeves");
        person1.setGender("Male");
        person1.setBirthDate(dateFormat.parse("1964-09-02"));
        personRepository.save(person1);

        person2 = new Person();
        person2.setFirstName("Laurence");
        person2.setLastName("Fishburne");
        person2.setGender("Male");
        person2.setBirthDate(dateFormat.parse("1961-07-30"));
        personRepository.save(person2);

        movie1 = new Movie();
        movie1.setTitle("The Matrix");
        movie1.setReleaseYear(dateFormat.parse("1999-03-31"));
        movieRepository.save(movie1);
    }

    @Test
    @Transactional
    void testGetPersons() throws Exception {


        mockMvc.perform(get("/person")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Keanu"))
                .andExpect(jsonPath("$[0].birthDate").value("1964-09-02T00:00:00.000+00:00")) // Date format
                .andExpect(jsonPath("$[1].firstName").value("Laurence"))
                .andExpect(jsonPath("$[1].birthDate").value("1961-07-30T00:00:00.000+00:00")); // Date format
    }

    @Test
    @Transactional
    void testGetPersonByFirstName() throws Exception {
        mockMvc.perform(get("/person/firstName/Keanu").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Keanu"))
                .andExpect(jsonPath("$.birthDate").value("1964-09-02T00:00:00.000+00:00")); // Date format
    }

    @Test
    @Transactional
    void testGetPersonByLastName() throws Exception {
        mockMvc.perform(get("/person/lastName/Fishburne").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Fishburne"))
                .andExpect(jsonPath("$.birthDate").value("1961-07-30T00:00:00.000+00:00")); // Date format
    }

    @Test
    @Transactional
    void testGetPersonByBirthDate() throws Exception {

        mockMvc.perform(get("/person/birthDate/1964-09-02")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Keanu"))
                .andExpect(jsonPath("$.birthDate").value("1964-09-02T00:00:00.000+00:00")); // Date format
    }

    @Test
    @Transactional
    void testAddPerson() throws Exception {
        PersonDTO newPersonDTO = new PersonDTO();
        newPersonDTO.setFirstName("Carrie");
        newPersonDTO.setLastName("Moss");
        newPersonDTO.setGender("Female");
        newPersonDTO.setBirthDate(dateFormat.parse("1967-08-21")); // Still using LocalDate in DTO

        String personJson = objectMapper.writeValueAsString(newPersonDTO);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").exists())
                .andExpect(jsonPath("$.firstName").value("Carrie"))
                .andExpect(jsonPath("$.birthDate").value("1967-08-21T00:00:00.000+00:00")); // Date format
    }

    @Test
    @Transactional
    void testUpdatePerson() throws Exception {
        UUID personId = person1.getPersonId();
        PersonDTO updatedPersonDTO = new PersonDTO();
        updatedPersonDTO.setFirstName("John");
        updatedPersonDTO.setLastName("Wick");
        updatedPersonDTO.setBirthDate(dateFormat.parse("1967-09-02"));

        String personJson = objectMapper.writeValueAsString(updatedPersonDTO);

        mockMvc.perform(put("/person/" + personId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(personId.toString()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.birthDate").value("1967-09-02T00:00:00.000+00:00")); // Date format
    }

    @Test
    @Transactional
    void testDeletePerson() throws Exception {
        UUID personId = person1.getPersonId();

        mockMvc.perform(delete("/person/" + personId).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(get("/person/" + personId).header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void testGetPersonById() throws Exception {
        mockMvc.perform(get("/person/" + person1.getPersonId()).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(person1.getPersonId().toString()))
                .andExpect(jsonPath("$.firstName").value("Keanu"))
                .andExpect(jsonPath("$.birthDate").value("1964-09-02T00:00:00.000+00:00")); // Date format
    }

}