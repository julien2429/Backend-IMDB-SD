package UTCN_IMDB.demo.DTO;

import java.util.List;
import java.util.UUID;

public class MovieRoleDTO {

    private String roleName;
    private UUID movieId;
    private String movieName;
    private UUID movieCastId;
    private String actorName;

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public UUID getMovieCastId() {
        return movieCastId;
    }

    public void setMovieCastId(UUID movieCastId) {
        this.movieCastId = movieCastId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public UUID getMovieId() {
        return movieId;
    }

    public void setMovieId(UUID movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
}
