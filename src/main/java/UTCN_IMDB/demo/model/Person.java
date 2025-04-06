package UTCN_IMDB.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "Person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID personId;
    @Column(name = "firstName", nullable = false)
    private String firstName;
    @Column(name = "lastName", nullable = false)
    private String lastName;
    @Column(name = "gender", nullable = false)
    private String gender;
    @Column(name = "nationality", nullable = true)
    private String nationality;
    @Column(name = "birthDate", nullable = true)
    private Date birthDate;
    @Column(name = "deathDate", nullable = true)
    private Date deathDate;

    @JsonManagedReference(value = "person-movieCasts") // Added the value
    @OneToMany(mappedBy = "person")
    private List<MovieCast> movieCastList;

    @Column(name = "imageUrl", nullable = true)
    private String imageUrl;

}