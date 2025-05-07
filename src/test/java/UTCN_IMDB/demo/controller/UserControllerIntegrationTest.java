package UTCN_IMDB.demo.controller;


import UTCN_IMDB.demo.DTO.ReviewDTO;
import UTCN_IMDB.demo.config.BCryptHashing;
import UTCN_IMDB.demo.enums.UserRole;
import UTCN_IMDB.demo.model.User;
import UTCN_IMDB.demo.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerIntegrationTest {

    /// BE SURE TO ADD ENV VARIABLES
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private static final String FIXTURE_PATH = "src/test/resources/fixtures/user/";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String token;

    @BeforeEach
    @Transactional
    void setUp() throws Exception {
        userRepository.deleteAll();
        //userRepository.flush();
        seedDatabase();
        createAndLoginAdminUser();
    }

    @Transactional
    protected void seedDatabase() throws Exception {
        String seedDataJson = loadFixture("user_seed.json");
        List<User> users = objectMapper.readValue(seedDataJson, new TypeReference<List<User>>() {});
        userRepository.saveAll(users);
    }


    @Transactional
    protected void createAndLoginAdminUser() throws Exception {

        MvcResult result = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "username": "admin",
                        "password": "admin12345"
                    }
                """))
                .andExpect(status().isOk())
                .andReturn();;

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        this.token = jsonNode.get("token").asText();
    }


    @Test
    @Transactional
    void testGetUsers() throws Exception {
        mockMvc.perform(get("/user").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()")
                        .value(2))
                .andExpect(jsonPath("$[*].username",
                        Matchers.containsInAnyOrder("admin", "user")))
                .andExpect(jsonPath("$[*].role",
                        Matchers.containsInAnyOrder(
                                "ADMIN", "UNKNOWN"
                        )))
                .andExpect(jsonPath("$[*].email",
                        Matchers.containsInAnyOrder(
                                "admin@admin.com", "user@user.com"
                        )));
    }


    @Test
    @Transactional
    void testAddPerson_ValidPayload() throws Exception {
        String validPersonJson = loadFixture("valid_user.json");

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPersonJson).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.username").value("LucaTheGreat"))
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.email").value("luca@gmail.com"));
    }

    @Test
    @Transactional
    void testAddPerson_InvalidPayload() throws Exception {
        String invalidPersonJson = loadFixture("invalid_user.json");

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPersonJson).header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username")
                        .value("Username should be between 3 and 20 characters"))
                .andExpect(jsonPath("$.password")
                        .value("Password should be between 6 and 20 characters"));

    }

    @Test
    @Transactional
    void testLogin_ValidPayload() throws Exception {
        String validLoginJson = loadFixture("login_user_valid.json");

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validLoginJson).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @Transactional
    void testLogin_InvalidPayload() throws Exception {
        String validLoginJson = loadFixture("login_user_invalid.json");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validLoginJson).header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    void testLogin_InvalidPassword() throws Exception {
        String validLoginJson = loadFixture("login_user_worng_password.json");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validLoginJson).header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    /**
     * Helper method to load JSON fixture from file.
     */
    @Transactional
    protected String loadFixture(String fileName) throws IOException {
        return Files.readString(Paths.get(FIXTURE_PATH + fileName));
    }



}
