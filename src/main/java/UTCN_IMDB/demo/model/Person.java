package UTCN_IMDB.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
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
    private LocalDate birthDate;
    @Column(name = "deathDate", nullable = true)
    private LocalDate deathDate;

    @OneToMany(mappedBy = "person")
    private List<MovieCast> movieCastList;

}
