package UTCN_IMDB.demo.controller;

import UTCN_IMDB.demo.DTO.MovieDTO;
import UTCN_IMDB.demo.DTO.ReviewDTO;
import UTCN_IMDB.demo.DTO.UserDTO;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.enums.UserRole;
import UTCN_IMDB.demo.model.*;
import UTCN_IMDB.demo.repository.*;
import UTCN_IMDB.demo.service.MovieService;
import UTCN_IMDB.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class MovieControllerIntegrationTest {


    /// BE SURE TO ADD ENV VARIABLES
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MovieGenreRepository movieGenreRepository;

    @Autowired
    private MovieCastRepository movieCastRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PersonRepository personRepository;



    @Autowired
    private MovieService movieService;

    private static final String FIXTURE_PATH = "src/test/resources/fixtures/";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private Movie movie1;
    private Movie movie2;
    private Genre genreAction;
    private Genre genreComedy;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        // Ensure the ObjectMapper is configured with JavaTimeModule for tests
        objectMapper.registerModule(new Jdk8Module());
    }


    @BeforeEach
    @Transactional
    void setUp() throws Exception {
        movieRepository.deleteAll();
        genreRepository.deleteAll();
        movieGenreRepository.deleteAll();
        movieCastRepository.deleteAll();
        roleRepository.deleteAll();
        personRepository.deleteAll();

        personRepository.flush();
        movieRepository.flush();
        genreRepository.flush();
        movieGenreRepository.flush();
        movieCastRepository.flush();
        roleRepository.flush();
        personRepository.flush();

        dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        seedDatabase();
    }

    @Transactional
    protected void seedDatabase() throws Exception {
        genreAction = new Genre();
        genreAction.setTitle("Action");
        genreAction= genreRepository.save(genreAction);

        genreComedy = new Genre();
        genreComedy.setTitle("Comedy");
        genreComedy = genreRepository.save(genreComedy);

        movie1 = new Movie();
        movie1.setTitle("The Matrix");
        movie1.setReleaseYear(dateFormat.parse("1999-03-31"));
        movie1.setDescription("A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.");
        List<MovieGenre> movieGenres = new ArrayList<>();
        movieGenres.add(new MovieGenre(movie1, genreAction));
        movie1.setMovieGenres(movieGenres);
        movie1= movieRepository.save(movie1);

        movie2 = new Movie();
        movie2.setTitle("Rush Hour");
        movie2.setReleaseYear(dateFormat.parse("1998-09-18"));
        movie2.setDescription("A Hong Kong inspector teams up with a loudmouthed LAPD detective to rescue the Chinese consul's kidnapped daughter.");
        List<MovieGenre> movieGenres2 = new ArrayList<>();
        movieGenres2.add(new MovieGenre(movie2, genreComedy));
        movieGenres2.add(new MovieGenre(movie2, genreAction));
        movie2.setMovieGenres(movieGenres2);
        movie2 = movieRepository.save(movie2);
    }

    private String loadFixture(String fileName) throws IOException {
        return Files.readString(Paths.get(FIXTURE_PATH + fileName));
    }

    @Test
    @Transactional
    void testGetMovies() throws Exception {
        mockMvc.perform(get("/movie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("The Matrix"))
                .andExpect(jsonPath("$[1].title").value("Rush Hour"));
    }

    @Test
    @Transactional
    void testAddMovie() throws Exception {
        MovieDTO newMovie = new MovieDTO();
        newMovie.setTitle("New Movie");
        newMovie.setReleaseYear(dateFormat.parse("2023-01-15"));

        String movieJson = objectMapper.writeValueAsString(newMovie);

        mockMvc.perform(post("/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId").exists())
                .andExpect(jsonPath("$.title").value("New Movie"))
                .andExpect(jsonPath("$.releaseYear").value("2023-01-15T00:00:00.000+00:00"));
    }

    @Test
    @Transactional
    void testUpdateMovie() throws Exception {
        UUID movieId = movie1.getMovieId();


        String movieJson = objectMapper.writeValueAsString(new MovieDTO("Updated Matrix", dateFormat.parse("2000-01-01")));

        mockMvc.perform(post("/movie/" + movieId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId").value(movieId.toString()))
                .andExpect(jsonPath("$.title").value("Updated Matrix"))
                .andExpect(jsonPath("$.releaseYear").value("2000-01-01T00:00:00.000+00:00"));
    }

    @Test
    @Transactional
    void testDeleteMovie() throws Exception {
        UUID movieId = movie1.getMovieId();

        mockMvc.perform(delete("/movie/" + movieId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/movie/" + movieId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void testGetMovieById() throws Exception {
        mockMvc.perform(get("/movie/" + movie1.getMovieId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId").value(movie1.getMovieId().toString()))
                .andExpect(jsonPath("$.title").value("The Matrix"));
    }

    @Test
    @Transactional
    void testAddGenreToMovie() throws Exception {
        UUID movieId = movie1.getMovieId();
        UUID genreId = genreComedy.getGenreId();
        List<String> genreIds = new ArrayList<>();
        genreIds.add(genreId.toString());
        String genreIdsJson = objectMapper.writeValueAsString(genreIds);

        mockMvc.perform(post("/movie/addGenre/" + movieId )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(genreIdsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId").value(movieId.toString()))
                .andExpect(jsonPath("$.movieGenres.length()").value(1))
                .andExpect(jsonPath("$.movieGenres[0].movieGenreId").exists());
    }

    @Test
    @Transactional
    void testGetMoviesByTitle() throws Exception {
        mockMvc.perform(get("/movie/title/The"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("The Matrix"));
    }



    @Test
    @Transactional
    void testGetMovieDetails() throws Exception {
        mockMvc.perform(get("/movie/details/" + movie1.getMovieId() ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId").value(movie1.getMovieId().toString()))
                .andExpect(jsonPath("$.title").value("The Matrix"))
                .andExpect(jsonPath("$.genre[0].title").value("Action"));
    }

    @Test
    @Transactional
    void testFilterMoviesByTitle() throws Exception {
        String filterJson = objectMapper.writeValueAsString(new MovieFilers("Matrix", null, null, null, null));
        mockMvc.perform(post("/movie/filterMovies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filterJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("The Matrix"));
    }

    @Test
    @Transactional
    void testFilterMoviesByGenre() throws Exception {
        String filterJson = objectMapper.writeValueAsString(new MovieFilers(null, List.of("Comedy"), null, null, null));
        mockMvc.perform(post("/movie/filterMovies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filterJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Rush Hour"));
    }

    @Test
    @Transactional
    void testFilterMoviesByYearRange() throws Exception {
        String filterJson = objectMapper.writeValueAsString(new MovieFilers(null, null, null, dateFormat.parse("1998-01-01"), dateFormat.parse("1998-12-31")));
        mockMvc.perform(post("/movie/filterMovies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filterJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Rush Hour"));
    }

    @Test
    @Transactional
    void testMovieDetails() throws Exception {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("Inception");
        movieDTO.setDescription("A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.");
        movieDTO.setReleaseYear(dateFormat.parse("2010-07-16"));
        Movie movie = movieService.addMovie(movieDTO);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password");
        userDTO.setEmail("test@gmail.com");
        userDTO.setRole(UserRole.NORMAL);
        User user = userService.addUser(userDTO);


        UserDTO userDTO2 = new UserDTO();
        userDTO2.setUsername("testuser2");
        userDTO2.setPassword("password2");
        userDTO2.setEmail("test2@gmail.com");
        userDTO2.setRole(UserRole.REVIEWER);
        User user2 = userService.addUser(userDTO2);

        ReviewDTO reviewDTO = new ReviewDTO(movie.getMovieId());
        reviewDTO.setRating(4.5f);
        reviewDTO.setReviewText("Great movie!");
        String reviewJson = objectMapper.writeValueAsString(reviewDTO);

        ReviewDTO reviewDTO2 = new ReviewDTO(movie.getMovieId());
        reviewDTO2.setRating(3.5f);
        reviewDTO2.setReviewText("Good movie!");
        String reviewJson2 = objectMapper.writeValueAsString(reviewDTO2);

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setGender("Male");
        person.setBirthDate(dateFormat.parse("1990-01-01"));
        person = personRepository.save(person);

        mockMvc.perform(post("/user/addReview/" + user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJson))
                .andExpect(status().isOk());

        mockMvc.perform(post("/user/addReview/" + user2.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJson2))
                .andExpect(status().isOk());

        movieService.addRoleToMovie(movie.getMovieId(), person.getPersonId(), "Neo");

        mockMvc.perform(get("/movie/details/" + movie.getMovieId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId").value(movie.getMovieId().toString()))
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.averageUserRating").value(4.5))
                .andExpect(jsonPath("$.averageCriticRating").value(3.5))
                .andExpect(jsonPath("$.averageRating").value(4.0))
                .andExpect(jsonPath("$.userReviews[0].comment").value("Great movie!"))
                .andExpect(jsonPath("$.actorsAndRoles[0].actor.firstName").value("John"))
                .andExpect(jsonPath("$.actorsAndRoles[0].actor.lastName").value("Doe"))
                .andExpect(jsonPath("$.actorsAndRoles[0].roles[0].roleName").value("Neo"));
    }

}