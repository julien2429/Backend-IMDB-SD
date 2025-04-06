package UTCN_IMDB.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "list_movie") // This will now be our explicit intermediary table
public class MovieList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID movieListId;

    @ManyToOne
    @JoinColumn(name = "list_id", nullable = false)
    @JsonBackReference(value = "list-movieLists") // Back reference from MovieList to Lists
    private Lists list;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @JsonBackReference(value = "movie-movieLists") // Back reference from MovieList to Movie
    private Movie movie;

}