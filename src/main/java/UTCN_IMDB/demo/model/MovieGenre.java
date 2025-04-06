package UTCN_IMDB.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "MovieGenre")
public class MovieGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID movieGenreId;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @JsonBackReference(value = "movie-movieGenres")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    @JsonBackReference(value = "genre-movieGenres")
    private Genre genre;


    public MovieGenre() {
    }

    public MovieGenre(Movie movie, Genre genre) {
        this.movie = movie;
        this.genre = genre;
    }



}