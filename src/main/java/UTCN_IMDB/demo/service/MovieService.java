package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.model.Genre;
import UTCN_IMDB.demo.model.Movie;
import UTCN_IMDB.demo.repository.GenreRepository;
import UTCN_IMDB.demo.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie updateMovie(UUID uuid, Movie movie) {
        Movie existingMovie =
                movieRepository.findById(uuid).orElseThrow(
                        () -> new IllegalStateException("Movie with uuid " + uuid + " not found"));
        existingMovie.setTitle(movie.getTitle());
        existingMovie.setMovieId(movie.getMovieId());
        return movieRepository.save(existingMovie);
    }

    public void deleteMovie(UUID uuid) {
        movieRepository.deleteById(uuid);
    }

    public Movie getMovieById(UUID uuid) {
        return movieRepository.findById(uuid).orElseThrow(
                () -> new IllegalStateException("Movie with uuid " + uuid + " not found"));
    }

//    public List<Movie> getMoviesByGenre(Genre genre) {
//        return movieRepository.findByGenres(genre);
//    }
//
//    public List<Movie> getMoviesByTitle(String title) {
//        return movieRepository.findByTitle(title);
//    }
//
//    public List<Movie> getMoviesByYear(int year) {
//        return movieRepository.findByYear(year);
//    }





}
