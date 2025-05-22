package UTCN_IMDB.demo.repository;

import UTCN_IMDB.demo.enums.ReviewStatus;
import UTCN_IMDB.demo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> getReviewsByStatus(ReviewStatus reviewStatus);
}
