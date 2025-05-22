package UTCN_IMDB.demo.model;

import UTCN_IMDB.demo.DTO.MovieRoleDTO;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ActorDetails {

    private UUID personId;

    private String firstName;

    private String lastName;

    private String gender;

    private String nationality;

    private Date birthDate;

    private Date deathDate;

    private List<MovieRoleDTO> movieRoles;

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public List<MovieRoleDTO> getMovieRoles() {
        return movieRoles;
    }

    public void setMovieRoles(List<MovieRoleDTO> movieRoles) {
        this.movieRoles = movieRoles;
    }
}
