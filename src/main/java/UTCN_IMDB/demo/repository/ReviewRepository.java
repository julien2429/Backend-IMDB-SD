package UTCN_IMDB.demo.repository;

import UTCN_IMDB.demo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

}
