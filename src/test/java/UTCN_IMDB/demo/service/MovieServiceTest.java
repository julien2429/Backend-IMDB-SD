package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.DTO.MovieDTO;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.model.*;
import UTCN_IMDB.demo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private MovieCastRepository movieCastRepository;
    @Mock
    private MovieGenreRepository movieGenreRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private S3Service s3Service;

    @InjectMocks
    private MovieService movieService;

    private Movie movie1;
    private Movie movie2;
    private Genre genre1;
    private Genre genre2;
    private Person person1;
    private Role role1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        movie1 = new Movie();
        movie1.setMovieId(UUID.randomUUID());
        movie1.setTitle("Movie 1");
        movie1.setReleaseYear(new Date(2020 - 1900, Calendar.JANUARY, 1));
        movie1.setMovieGenres(new ArrayList<>());
        movie1.setMovieCastList(new ArrayList<>());

        movie2 = new Movie();
        movie2.setMovieId(UUID.randomUUID());
        movie2.setTitle("Movie 2");
        movie2.setReleaseYear(new Date(2022 - 1900, Calendar.FEBRUARY, 15));
        movie2.setMovieGenres(new ArrayList<>());
        movie2.setMovieCastList(new ArrayList<>());

        genre1 = new Genre();
        genre1.setGenreId(UUID.randomUUID());
        genre1.setTitle("Action");

        genre2 = new Genre();
        genre2.setGenreId(UUID.randomUUID());
        genre2.setTitle("Comedy");

        person1 = new Person();
        person1.setPersonId(UUID.randomUUID());
        person1.setFirstName("John");
        person1.setLastName("Doe");

        role1 = new Role();
        role1.setRoleId(UUID.randomUUID());
        role1.setRoleName("Actor");
    }

    @Test
    void getMovies() {
        // given
        List<Movie> movies = List.of(movie1, movie2);
        when(movieRepository.findAll()).thenReturn(movies);

        // when
        List<Movie> result = movieService.getMovies();

        // then
        assertEquals(2, result.size());
        assertEquals(movies, result);
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void addMovie() {
        // given
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("New Movie");
        movieDTO.setReleaseYear(new Date(2024 - 1900, Calendar.MARCH, 10));

        Movie savedMovie = new Movie();
        savedMovie.setMovieId(UUID.randomUUID());
        savedMovie.setTitle(movieDTO.getTitle());
        savedMovie.setReleaseYear(movieDTO.getReleaseYear());
        savedMovie.setMovieGenres(new ArrayList<>());
        savedMovie.setMovieCastList(new ArrayList<>());

        when(movieRepository.save(any(Movie.class))).thenReturn(savedMovie);

        // when
        Movie result = movieService.addMovie(movieDTO);

        // then
        assertEquals(movieDTO.getTitle(), result.getTitle());
        assertEquals(movieDTO.getReleaseYear(), result.getReleaseYear());
        assertNotNull(result.getMovieId());
        assertTrue(result.getMovieGenres().isEmpty());
        assertTrue(result.getMovieCastList().isEmpty());
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void updateMovie_existingMovie() throws CompileTimeException {
        // given
        UUID movieId = movie1.getMovieId();
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("Updated Movie");
        movieDTO.setReleaseYear(new Date(2025 - 1900, Calendar.APRIL, 1));

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie1));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie1);

        // when
        Movie result = movieService.updateMovie(movieId, movieDTO);

        // then
        assertEquals(movieDTO.getTitle(), result.getTitle());
        assertEquals(movieDTO.getReleaseYear(), result.getReleaseYear());
        assertEquals(movieId, result.getMovieId());
        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, times(1)).save(movie1);
    }

    @Test
    void updateMovie_nonExistingMovie() {
        // given
        UUID movieId = UUID.randomUUID();
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("Updated Movie");
        movieDTO.setReleaseYear(new Date(2025 - 1900, Calendar.APRIL, 1));

        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompileTimeException.class, () -> movieService.updateMovie(movieId, movieDTO));
        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void deleteMovie_existingMovie() {
        // given
        UUID movieId = movie1.getMovieId();
        doNothing().when(movieRepository).deleteById(movieId);

        // when
        movieService.deleteMovie(movieId);

        // then
        verify(movieRepository, times(1)).deleteById(movieId);
    }

    @Test
    void getMovieById_existingMovie() throws CompileTimeException {
        // given
        UUID movieId = movie1.getMovieId();
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie1));

        // when
        Movie result = movieService.getMovieById(movieId);

        // then
        assertEquals(movie1, result);
        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    void getMovieById_nonExistingMovie() {
        // given
        UUID movieId = UUID.randomUUID();
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompileTimeException.class, () -> movieService.getMovieById(movieId));
        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    void getMoviesByYear() {
        // given
        Date year = new Date(2020 - 1900, Calendar.JANUARY, 1);
        List<Movie> movies = List.of(movie1);
        when(movieRepository.findByReleaseYear(year)).thenReturn(movies);

        // when
        List<Movie> result = movieService.getMoviesByYear(year);

        // then
        assertEquals(1, result.size());
        assertEquals(movie1, result.get(0));
        verify(movieRepository, times(1)).findByReleaseYear(year);
    }

    @Test
    void addGenreToMovie_existingMovieAndGenres() throws CompileTimeException {
        // given
        UUID movieId = movie1.getMovieId();
        List<UUID> genreUUIDs = List.of(genre1.getGenreId(), genre2.getGenreId());

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie1));
        when(genreRepository.findById(genre1.getGenreId())).thenReturn(Optional.of(genre1));
        when(genreRepository.findById(genre2.getGenreId())).thenReturn(Optional.of(genre2));
        when(movieRepository.save(movie1)).thenReturn(movie1);

        // when
        Movie result = movieService.addGenreToMovie(movieId, genreUUIDs);

        // then
        assertEquals(2, result.getMovieGenres().size());
        List<Genre> genres = result.getMovieGenres().stream().map(MovieGenre::getGenre).toList();
        assertTrue(genres.contains(genre1));
        assertTrue(genres.contains(genre2));
        verify(movieRepository, times(1)).findById(movieId);
        verify(genreRepository, times(1)).findById(genre1.getGenreId());
        verify(genreRepository, times(1)).findById(genre2.getGenreId());
        verify(movieRepository, times(1)).save(movie1);
    }

    @Test
    void addGenreToMovie_nonExistingMovie() {
        // given
        UUID movieId = UUID.randomUUID();
        List<UUID> genreUUIDs = List.of(genre1.getGenreId());

        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompileTimeException.class, () -> movieService.addGenreToMovie(movieId, genreUUIDs));
        verify(movieRepository, times(1)).findById(movieId);
        verify(genreRepository, never()).findById(any());
        verify(movieRepository, never()).save(any());
    }

    @Test
    void addGenreToMovie_nonExistingGenre() {
        // given
        UUID movieId = movie1.getMovieId();
        UUID nonExistingGenreUUID = UUID.randomUUID();
        List<UUID> genreUUIDs = List.of(nonExistingGenreUUID);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie1));
        when(genreRepository.findById(nonExistingGenreUUID)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompileTimeException.class, () -> movieService.addGenreToMovie(movieId, genreUUIDs));
        verify(movieRepository, times(1)).findById(movieId);
        verify(genreRepository, times(1)).findById(nonExistingGenreUUID);
        verify(movieRepository, never()).save(any());
    }

    @Test
    void getMoviesByTitle() {
        // given
        String title = "Movie";
        List<Movie> movies = List.of(movie1, movie2);
        when(movieRepository.findByTitleStartsWith(title)).thenReturn(movies);

        // when
        List<Movie> result = movieService.getMoviesByTitle(title);

        // then
        assertEquals(2, result.size());
        assertEquals(movies, result);
        verify(movieRepository, times(1)).findByTitleStartsWith(title);
    }

    @Test
    void getMoviesByGenre_singleGenreMatch() {
        // given
        MovieGenre movieGenre1 = new MovieGenre();
        movieGenre1.setMovie(movie1);
        movieGenre1.setGenre(genre1);
        movie1.setMovieGenres(List.of(movieGenre1));
        List<String> genres = List.of("Action");
        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));

        // when
        List<Movie> result = movieService.getMoviesByGenre(genres);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(movie1));
    }

    @Test
    void getMoviesByGenre_multipleGenresMatchSameMovie() {
        // given
        MovieGenre movieGenre1 = new MovieGenre();
        movieGenre1.setMovie(movie1);
        movieGenre1.setGenre(genre1);
        MovieGenre movieGenre2 = new MovieGenre();
        movieGenre2.setMovie(movie1);
        movieGenre2.setGenre(genre2);
        movie1.setMovieGenres(List.of(movieGenre1, movieGenre2));

        List<String> genres = List.of("Action", "Comedy");
        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));

        // when
        List<Movie> result = movieService.getMoviesByGenre(genres);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(movie1));
    }

    @Test
    void getMoviesByGenre_multipleGenresMatchDifferentMovies() {
        // given
        MovieGenre movieGenre1 = new MovieGenre();
        movieGenre1.setMovie(movie1);
        movieGenre1.setGenre(genre1);
        MovieGenre movieGenre2 = new MovieGenre();
        movieGenre2.setMovie(movie2);
        movieGenre2.setGenre(genre2);
        movie1.setMovieGenres(List.of(movieGenre1));
        movie2.setMovieGenres(List.of(movieGenre2));

        List<String> genres = List.of("Action", "Comedy");
        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));

        // when
        List<Movie> result = movieService.getMoviesByGenre(genres);

        // then
        assertEquals(2, result.size());
        assertTrue(result.contains(movie1));
        assertTrue(result.contains(movie2));
    }

    @Test
    void getMoviesByGenre_noMatch() {
        // given
        List<String> genres = List.of("Sci-Fi");
        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));

        // when
        List<Movie> result = movieService.getMoviesByGenre(genres);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void getMoviesByYearRange() {
        // given
        Date startDate = new Date(2019 - 1900, Calendar.JANUARY, 1);
        Date endDate = new Date(2021 - 1900, Calendar.DECEMBER, 31);
        List<Movie> movies = List.of(movie1);
        when(movieRepository.findByReleaseYearBetween(startDate, endDate)).thenReturn(movies);

        // when
        List<Movie> result = movieService.getMoviesByYearRange(startDate, endDate);

        // then
        assertEquals(1, result.size());
        assertEquals(movie1, result.get(0));
        verify(movieRepository, times(1)).findByReleaseYearBetween(startDate, endDate);
    }

    @Test
    void addRoleToMovie_existingMovieAndPerson() throws CompileTimeException {
        // given
        UUID movieId = movie1.getMovieId();
        UUID personId = person1.getPersonId();
        String roleTitle = "Lead Actor";

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie1));
        when(personRepository.findByPersonId(personId)).thenReturn(Optional.of(person1));
        when(roleRepository.save(any(Role.class))).thenReturn(role1);
        when(movieCastRepository.save(any(MovieCast.class))).thenReturn(new MovieCast());
        when(movieRepository.save(movie1)).thenReturn(movie1);

        // when
        Movie result = movieService.addRoleToMovie(movieId, personId, roleTitle);

        // then
        assertEquals(1, result.getMovieCastList().size());
        assertEquals(person1, result.getMovieCastList().get(0).getPerson());
        assertEquals(roleTitle, result.getMovieCastList().get(0).getRole().getRoleName());
        verify(movieRepository, times(1)).findById(movieId);
        verify(personRepository, times(1)).findByPersonId(personId);
        verify(roleRepository, times(1)).save(any(Role.class));
        verify(movieCastRepository, times(1)).save(any(MovieCast.class));
        verify(movieRepository, times(1)).save(movie1);
    }

    @Test
    void addRoleToMovie_nonExistingMovie() {
        // given
        UUID movieId = UUID.randomUUID();
        UUID personId = person1.getPersonId();
        String roleTitle = "Lead Actor";

        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompileTimeException.class, () -> movieService.addRoleToMovie(movieId, personId, roleTitle));
        verify(movieRepository, times(1)).findById(movieId);
        verify(personRepository, never()).findByPersonId(any());
        verify(roleRepository, never()).save(any());
        verify(movieCastRepository, never()).save(any());
        verify(movieRepository, never()).save(any());
    }

    @Test
    void addRoleToMovie_nonExistingPerson() {
        // given
        UUID movieId = movie1.getMovieId();
        UUID personId = UUID.randomUUID();
        String roleTitle = "Lead Actor";

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie1));
        when(personRepository.findByPersonId(personId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(IllegalStateException.class, () -> movieService.addRoleToMovie(movieId, personId, roleTitle));
        verify(movieRepository, times(1)).findById(movieId);
        verify(personRepository, times(1)).findByPersonId(personId);
        verify(roleRepository, never()).save(any());
        verify(movieCastRepository, never()).save(any());
        verify(movieRepository, never()).save(any());
    }



    @Test
    void getMovieDetails_existingMovie_noReviews() throws CompileTimeException {
        // given
        UUID movieId = movie1.getMovieId();
        movie1.setMovieCastList(new ArrayList<>());
        movie1.setMovieGenres(new ArrayList<>());
        movie1.setReviews(new ArrayList<>()); // Initialize the reviews list

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie1));

        // when
        MovieDetails result = movieService.getMovieDetails(movieId);

        // then
        assertEquals(movieId, result.getMovieId());
        assertEquals(movie1.getTitle(), result.getTitle());
        assertEquals(movie1.getReleaseYear(), result.getYear());
        assertEquals(0f, result.getAverageRating());
        assertEquals(0f, result.getAverageCriticRating());
        assertTrue(result.getActorsAndRoles().isEmpty());
        assertNull(result.getDirector());
        assertTrue(result.getGenre().isEmpty());
        verify(movieRepository, times(1)).findById(movieId);
    }



    @Test
    void getMovieDetails_nonExistingMovie()  {
        // given
        UUID movieId = UUID.randomUUID();
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompileTimeException.class, () -> movieService.getMovieDetails(movieId));
        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    void filterMovies_noCriteria() {
        // given
        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));
        MovieFilers movieFilers = new MovieFilers(); // All Optionals are empty

        // when
        List<Movie> result = movieService.filterMovies(movieFilers);

        // then
        assertEquals(2, result.size());
        assertTrue(result.contains(movie1));
        assertTrue(result.contains(movie2));
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void filterMovies_byTitle() {
        // given
        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));
        MovieFilers movieFilers = new MovieFilers();
        movieFilers.setTitle(Optional.of("Movie 1"));

        // when
        List<Movie> result = movieService.filterMovies(movieFilers);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(movie1));
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void filterMovies_byEndDate() {
        // given
        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));
        MovieFilers movieFilers = new MovieFilers();
        movieFilers.setEndDate(Optional.of(new Date(2021 - 1900, Calendar.DECEMBER, 31)));

        // when
        List<Movie> result = movieService.filterMovies(movieFilers);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(movie1));
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void filterMovies_byStartDate() {
        // given
        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));
        MovieFilers movieFilers = new MovieFilers();
        movieFilers.setStartDate(Optional.of(new Date(2021 - 1900, Calendar.JANUARY, 1)));

        // when
        List<Movie> result = movieService.filterMovies(movieFilers);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(movie2));
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void filterMovies_byGenresNames() {
        // given

        MovieGenre movieGenre1 = new MovieGenre();
        movieGenre1.setMovie(movie1);
        movieGenre1.setGenre(genre1);
        MovieGenre movieGenre2 = new MovieGenre();
        movieGenre2.setMovie(movie2);
        movieGenre2.setGenre(genre2);

        movie1.setMovieGenres(List.of(movieGenre1));
        movie2.setMovieGenres(List.of(movieGenre2));

        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));
        MovieFilers movieFilers = new MovieFilers();
        movieFilers.setGenresNames(Optional.of(List.of("Action")));

        // when
        List<Movie> result = movieService.filterMovies(movieFilers);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(movie1));
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void filterMovies_multipleCriteria() {
        // given

        MovieGenre movieGenre1 = new MovieGenre();
        movieGenre1.setMovie(movie1);
        movieGenre1.setGenre(genre1);
        MovieGenre movieGenre2 = new MovieGenre();
        movieGenre2.setMovie(movie2);
        movieGenre2.setGenre(genre2);
        movie1.setMovieGenres(List.of(movieGenre1));

        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));
        MovieFilers movieFilers = new MovieFilers();
        movieFilers.setTitle(Optional.of("Movie 1"));
        movieFilers.setGenresNames(Optional.of(List.of("Action")));
        movieFilers.setEndDate(Optional.of(new Date(2021 - 1900, Calendar.DECEMBER, 31)));

        // when
        List<Movie> result = movieService.filterMovies(movieFilers);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(movie1));
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void filterMovies_noMatchWithCriteria() {
        // given
        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));
        MovieFilers movieFilers = new MovieFilers();
        movieFilers.setTitle(Optional.of("NonExistent"));

        // when
        List<Movie> result = movieService.filterMovies(movieFilers);

        // then
        assertTrue(result.isEmpty());
        verify(movieRepository, times(1)).findAll();
    }
}