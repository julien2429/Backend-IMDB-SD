package UTCN_IMDB.demo.model;

import UTCN_IMDB.demo.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID reviewId;

    @Column(name = "rating", nullable = false)
    private float rating;

    @Column(name = "comment", nullable = true)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonBackReference(value = "movie-reviews")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-reviews")
    private User user;

    @Transient
    public UserRole getUserRole() {
        return user.getRole();
    }


}
