package UTCN_IMDB.demo.utils;

import UTCN_IMDB.demo.enums.ReviewStatus;
import UTCN_IMDB.demo.enums.UserRole;
import UTCN_IMDB.demo.model.*;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsUtils {

    public static List<Review> getUserReviews(List<Review> reviews) {

        if (reviews == null || reviews.isEmpty()) {
            return new ArrayList<>();
        }

        return reviews.stream()
                .filter(review -> review.getUserRole() != UserRole.REVIEWER)
                .filter(review -> review.getStatus() == ReviewStatus.APPROVED)
                .toList();
    }



    public static  List<Review> getCriticReviews(List<Review> reviews) {

        if (reviews == null || reviews.isEmpty()) {
            return new ArrayList<>();
        }

        return reviews.stream()
                .filter(review -> review.getUserRole() == UserRole.REVIEWER)
                .filter(review -> review.getStatus() == ReviewStatus.APPROVED)
                .toList();
    }

    public static  double getAverageUserRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0;
        }
        return reviews.stream()
                .filter(review -> review.getUserRole() != UserRole.REVIEWER)
                .filter(review -> review.getStatus() == ReviewStatus.APPROVED)
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0);
    }

    public static  double getAverageCriticRating(List<Review> reviews) {

        if (reviews == null || reviews.isEmpty()) {
            return 0;
        }

        return reviews.stream()
                .filter(review -> review.getUserRole() == UserRole.REVIEWER)
                .filter(review -> review.getStatus() == ReviewStatus.APPROVED)
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0);
    }

    public static  double getAverageRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0;
        }
        return reviews.stream()
                .filter(review -> review.getStatus() == ReviewStatus.APPROVED)
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0);
    }

    public static  double calculateMean(List<Double> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return 0;
        }


        double sum = 0;
        for (double rating : ratings) {
            sum += rating;
        }
        return sum / ratings.size();
    }

    public static  double getMeanUserRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0;
        }

        List<Double> userRatings = reviews.stream()
                .filter(review -> review.getUserRole() != UserRole.REVIEWER)
                .filter(review -> review.getStatus() == ReviewStatus.APPROVED)
                .mapToDouble(Review::getRating)
                .boxed()
                .toList();

        return calculateMean(userRatings);

     }

    public static  double getMeanCriticRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0;
        }
        List<Double> criticRatings = reviews.stream()
                .filter(review -> review.getUserRole() == UserRole.REVIEWER)
                .filter(review -> review.getStatus() == ReviewStatus.APPROVED)
                .mapToDouble(Review::getRating)
                .boxed()
                .toList();

        return calculateMean(criticRatings);
    }

    public static  double getMeanRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0;
        }
        List<Double> allRatings = reviews.stream()
                .filter(review -> review.getStatus() == ReviewStatus.APPROVED)
                .mapToDouble(Review::getRating)
                .boxed()
                .toList();

        return calculateMean(allRatings);
    }


    public static  List<ActorRoles> getActorRoles(Movie movie) {


        if(movie.getMovieCastList() == null || movie.getMovieCastList().isEmpty())
        {
            return new ArrayList<>();
        }
        else
        {
            List< ActorRoles > actorsAndRoles = new ArrayList<>();
            for (MovieCast movieCast : movie.getMovieCastList()) {
                Person person = movieCast.getPerson();
                Role role = movieCast.getRole();
                boolean found = false;
                for (ActorRoles pair : actorsAndRoles) {
                    if (pair.getActor().equals(person)) {
                        pair.getRoles().add(role);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    List<Role> roles = new ArrayList<>();
                    roles.add(role);
                    ActorRoles actorRoles = new ActorRoles(person, roles);
                    actorsAndRoles.add(actorRoles);
                }
            }
            return actorsAndRoles;
        }
    }

    public static  String getDirector(Movie movie)
    {
        String director = null;

        if(movie.getMovieCastList() == null || movie.getMovieCastList().isEmpty())
        {
            return null;
        }
        else
        {
            for (MovieCast movieCast : movie.getMovieCastList()) {
                if (movieCast.getRole().getRoleName().equalsIgnoreCase("director")) {
                    director = movieCast.getPerson().getFirstName() + " " + movieCast.getPerson().getLastName();
                    break;
                }
            }
            return director;
        }
    }

    public static  List<Genre> getGenres(Movie movie) {
        List<Genre> genres = new ArrayList<>();
        if (movie.getMovieGenres() != null) {
            for (MovieGenre movieGenre : movie.getMovieGenres()) {
                genres.add(movieGenre.getGenre());
            }
        }
        return genres;
    }




}
