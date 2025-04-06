package UTCN_IMDB.demo.model;

import UTCN_IMDB.demo.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.mapping.Set;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "Genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID genreId;

    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "genre-movieGenres")
    private List<MovieGenre> movieGenres;
}
