package UTCN_IMDB.demo.repository;

import UTCN_IMDB.demo.model.Movie;
import UTCN_IMDB.demo.model.MovieCast;
import UTCN_IMDB.demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieCastRepository extends JpaRepository<MovieCast, UUID>  {

    Optional <List<MovieCast>> findByMovieAndPerson(Movie movie, Person person);

}
