package UTCN_IMDB.demo.repository;

import UTCN_IMDB.demo.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GenreRepository extends JpaRepository<Genre, UUID> {

    Optional<Genre> findByTitle(String title);
}
