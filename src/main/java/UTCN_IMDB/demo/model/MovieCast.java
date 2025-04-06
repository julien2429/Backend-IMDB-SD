package UTCN_IMDB.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "MovieCast")
public class MovieCast {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID castId;

    @JsonBackReference(value = "movie-movieCasts") // Assuming Movie has @JsonManagedReference("movie-movieCasts")
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @JsonBackReference(value = "person-movieCasts") // Assuming Person has @JsonManagedReference("person-movieCasts")
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @JsonBackReference(value = "role-movieCasts")   // Assuming Role has @JsonManagedReference("role-movieCasts")
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}