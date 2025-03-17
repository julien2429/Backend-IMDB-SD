package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.DTO.MovieDTO;
import UTCN_IMDB.demo.DTO.PersonDTO;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.model.Genre;
import UTCN_IMDB.demo.model.Movie;
import UTCN_IMDB.demo.model.Person;
import UTCN_IMDB.demo.model.Role;
import UTCN_IMDB.demo.repository.GenreRepository;
import UTCN_IMDB.demo.repository.MovieCastRepository;
import UTCN_IMDB.demo.repository.MovieRepository;
import ch.qos.logback.core.joran.sanity.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@AllArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieCastRepository movieCastRepository;

    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    public Movie addMovie(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setYear(movieDTO.getYear());
        movie.setMovieCastList(new ArrayList<>());
        movie.setGenres(new ArrayList<>());
        return movieRepository.save(movie);
    }

    public Movie updateMovie(UUID uuid, MovieDTO movieDTO) {
        Movie existingMovie =
                movieRepository.findById(uuid).orElseThrow(
                        () -> new IllegalStateException("Movie with uuid " + uuid + " not found"));

        existingMovie.setTitle(movieDTO.getTitle());
        existingMovie.setYear(movieDTO.getYear());
        existingMovie.setMovieId(uuid);
        return movieRepository.save(existingMovie);
    }

    public void deleteMovie(UUID uuid) {
        movieRepository.deleteById(uuid);
    }

    public Movie getMovieById(UUID uuid) {
        return movieRepository.findById(uuid).orElseThrow(
                () -> new IllegalStateException("Movie with uuid " + uuid + " not found"));
    }

    public List<Movie> getMoviesByYear(Date year) {
        return movieRepository.findByYear(year);
    }

    public Movie addGenreToMovie(UUID movieUUID, List<UUID> genreUUIDs) throws CompileTimeException {
        Movie movie = movieRepository.findById(movieUUID).orElseThrow(
                () -> new CompileTimeException("Movie with uuid " + movieUUID + " not found"));

        movie.setGenres(new ArrayList<>());

        for (UUID genreUUID : genreUUIDs) {
            Genre genre = genreRepository.findById(genreUUID)
                    .orElseThrow(() -> new CompileTimeException("Genre with uuid " + genreUUID + " not found"));
            movie.getGenres().add(genre);
        }

        return movieRepository.save(movie);
    }


    public List<Movie> getMoviesByTitle(String title) {
        return movieRepository.findByTitleStartsWith(title);
    }

    private List<String> getGenreTitles(Movie movie) {
        return  movie.getGenres().stream().map(Genre::getTitle).toList();
    }

    public List<Movie> getMoviesByGenre(List<String> genres) {
        List <Movie> movies = movieRepository.findAll();
        List<Movie> result = new ArrayList<>();

        genres.forEach(genre -> {
            List<Movie> foundMovies= movies.stream().filter(movie ->
                    getGenreTitles(movie).contains(genre)).toList();

            foundMovies.forEach(movie -> {
                if (!result.contains(movie)) {
                    result.add(movie);
                }
            });
        });

        return result;

    }

    public List<Movie> getMoviesByYearRange(Date startDate, Date endDate) {
        return movieRepository.findByYearBetween(startDate, endDate);
    }

}
