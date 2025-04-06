package UTCN_IMDB.demo.repository;

import UTCN_IMDB.demo.model.MovieGenre;
import UTCN_IMDB.demo.model.MovieList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovieListRepository extends JpaRepository<MovieList, UUID> {
}
