package UTCN_IMDB.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID roleId;

    @Column(name = "roleName", nullable = false)
    private String roleName;


    @OneToMany(mappedBy = "role")
    private List<MovieCast> movieCastsList;

}