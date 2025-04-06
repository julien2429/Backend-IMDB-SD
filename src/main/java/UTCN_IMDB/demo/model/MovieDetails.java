package UTCN_IMDB.demo.model;

import org.antlr.v4.runtime.misc.Pair;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MovieDetails {
    private UUID movieId;
    private String title;
    private String description;
    private List<Genre> genre;
    private String director;
    private List< ActorRoles > actorsAndRoles;
    private String imageUrl;
    private List<Review> userReviews;
    private List<Review> criticReviews;
    private Date year;

    private double averageUserRating;
    private double averageCriticRating;
    private double averageRating;
    private double meanUserRating;
    private double meanCriticRating;
    private double meanRating;


    public double getAverageUserRating() {
        return averageUserRating;
    }

    public void setAverageUserRating(double averageUserRating) {
        this.averageUserRating = averageUserRating;
    }

    public double getAverageCriticRating() {
        return averageCriticRating;
    }

    public void setAverageCriticRating(double averageCriticRating) {
        this.averageCriticRating = averageCriticRating;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public double getMeanUserRating() {
        return meanUserRating;
    }

    public void setMeanUserRating(double meanUserRating) {
        this.meanUserRating = meanUserRating;
    }

    public double getMeanCriticRating() {
        return meanCriticRating;
    }

    public void setMeanCriticRating(double meanCriticRating) {
        this.meanCriticRating = meanCriticRating;
    }

    public double getMeanRating() {
        return meanRating;
    }

    public void setMeanRating(double meanRating) {
        this.meanRating = meanRating;
    }

    public MovieDetails() {

    }

    public List<Review> getUserReviews() {
        return userReviews;
    }

    public void setUserReviews(List<Review> userReviews) {
        this.userReviews = userReviews;
    }

    public List<Review> getCriticReviews() {
        return criticReviews;
    }

    public void setCriticReviews(List<Review> criticReviews) {
        this.criticReviews = criticReviews;
    }

    public UUID getMovieId() {
        return movieId;
    }

    public void setMovieId(UUID movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Genre> getGenre() {
        return genre;
    }

    public void setGenre(List<Genre> genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public List< ActorRoles> getActorsAndRoles() {
        return actorsAndRoles;
    }

    public void setActorsAndRoles( List< ActorRoles> actorsAndRoles) {
        this.actorsAndRoles = actorsAndRoles;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
        this.year = year;
    }


// Getters and setters
}
