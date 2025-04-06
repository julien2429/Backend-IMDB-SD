package UTCN_IMDB.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "Movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID movieId;
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "release_Year")
    private Date releaseYear;

    @Column(name = "description", nullable = true)
    private String description;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "movie-movieGenres")
    private List<MovieGenre> movieGenres;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "movie-movieLists") // Managed reference from Movie to MovieList
    private List<MovieList> movieList;

    @OneToMany(mappedBy = "movie")
    @JsonManagedReference(value = "movie-movieCasts")
    private List<MovieCast> movieCastList;

    @OneToMany(mappedBy = "movie")
    @JsonManagedReference(value = "movie-reviews")
    private List<Review> reviews;

    @Column(name = "imageUrl", nullable = true)
    private String imageUrl;


}
