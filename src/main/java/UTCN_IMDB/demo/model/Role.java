package UTCN_IMDB.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @JsonManagedReference(value = "role-movieCasts") // Added the value
    @OneToMany(mappedBy = "role")
    private List<MovieCast> movieCastsList;

}