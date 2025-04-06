package UTCN_IMDB.demo.repository;
import UTCN_IMDB.demo.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Transactional
public interface MovieRepository extends JpaRepository<Movie, UUID> {
    List<Movie> findByTitleStartsWith(String title);
    List<Movie> findByReleaseYear(Date year);
    List<Movie> findByReleaseYearBetween(Date start, Date end);
}
