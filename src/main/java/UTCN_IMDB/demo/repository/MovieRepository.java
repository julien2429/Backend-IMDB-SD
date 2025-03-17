package UTCN_IMDB.demo.repository;

import UTCN_IMDB.demo.model.Genre;
import UTCN_IMDB.demo.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {
    List<Movie> findByTitleStartsWith(String title);
    List<Movie> findByYear(Date year);
    List<Movie> findByYearBetween(Date start, Date end);
//    Optional<Movie> findById(UUID uuid);
//    List<Movie> findByGenres(Genre genre);
//    List<Movie> findByTitle(String title);
//    List<Movie> findByYear(int year);
}
