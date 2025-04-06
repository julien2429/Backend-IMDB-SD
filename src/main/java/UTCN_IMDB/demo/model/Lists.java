package UTCN_IMDB.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference; // Changed to @JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "Lists")
public class Lists {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID listId;

    @Column(name = "listName", nullable = false)
    private String listName;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "list-movieLists")
    private List<MovieList> movieList;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-lists") // Now the back reference to User
    private User user;
}