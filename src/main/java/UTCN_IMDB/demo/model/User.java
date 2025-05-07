package UTCN_IMDB.demo.model;

import UTCN_IMDB.demo.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonManagedReference; // Changed to @JsonManagedReference
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "AppUser")
@NamedEntityGraph(name = "User.full", attributeNodes = {
        @NamedAttributeNode("reviews"),
        @NamedAttributeNode("lists")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    private UserRole role;

    @JsonManagedReference(value = "user-reviews")
    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @JsonManagedReference(value = "forgotten-passwords")
    @OneToMany(mappedBy = "user")
    private List<ForgottenPasswordData> forgottenPasswords;

    @Column(name = "imageUrl", nullable = true)
    private String imageUrl;

    @JsonManagedReference(value = "user-lists") // Now the managed reference for the list of Lists
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lists> lists;
}