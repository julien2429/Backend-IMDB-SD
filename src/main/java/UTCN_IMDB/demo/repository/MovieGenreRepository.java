package UTCN_IMDB.demo.repository;

import UTCN_IMDB.demo.model.Movie;
import UTCN_IMDB.demo.model.MovieCast;
import UTCN_IMDB.demo.model.MovieGenre;
import UTCN_IMDB.demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, UUID> {

    void deleteByMovie_MovieId(UUID movieId);

    Optional<MovieGenre> findMovieGenreByMovie_MovieIdAndGenre_GenreId(UUID movieId, UUID genreId);
}
