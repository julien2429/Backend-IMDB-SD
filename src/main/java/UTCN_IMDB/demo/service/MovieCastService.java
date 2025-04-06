package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.model.MovieCast;
import UTCN_IMDB.demo.repository.MovieCastRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MovieCastService {

    private final MovieCastRepository movieCastRepository;

    MovieCast getMovieCastById(UUID uuid) {
        return movieCastRepository.findById(uuid).orElseThrow(
                () -> new IllegalStateException("MovieCast with uuid " + uuid + " not found"));
    }

    void deleteMovieCast(UUID uuid) {
        movieCastRepository.deleteById(uuid);
    }

    MovieCast updateMovieCast(UUID uuid, MovieCast movieCast) {
        MovieCast existingMovieCast =
                movieCastRepository.findById(uuid).orElseThrow(
                        () -> new IllegalStateException("MovieCast with uuid " + uuid + " not found"));

        existingMovieCast.setMovie(movieCast.getMovie());
        existingMovieCast.setPerson(movieCast.getPerson());
        existingMovieCast.setRole(movieCast.getRole());
        return movieCastRepository.save(existingMovieCast);
    }

    MovieCast addMovieCast(MovieCast movieCast) {
        return movieCastRepository.save(movieCast);
    }

    public List<MovieCast> getMovieCasts() {
        return movieCastRepository.findAll();
    }




}
