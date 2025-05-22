package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.DTO.MovieDTO;
import UTCN_IMDB.demo.DTO.MovieRoleDTO;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.enums.ReviewStatus;
import UTCN_IMDB.demo.enums.UserRole;
import UTCN_IMDB.demo.model.*;
import UTCN_IMDB.demo.repository.*;
import UTCN_IMDB.demo.utils.MovieDetailsUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionException;


@Service
@AllArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieCastRepository movieCastRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;

    @Transactional
    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    public Movie addMovie(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setReleaseYear(movieDTO.getReleaseYear());
        movie.setMovieCastList(new ArrayList<>());
        movie.setMovieGenres(new ArrayList<>());
        movie.setReviews(new ArrayList<>());
        return movieRepository.save(movie);
    }

    @Transactional
    public Movie updateMovie(UUID uuid, MovieDTO movieDTO) throws CompileTimeException {
        Movie existingMovie =
                movieRepository.findById(uuid).orElseThrow(
                        () ->  new CompileTimeException("Movie with uuid " + uuid + " not found"));

        existingMovie.setTitle(movieDTO.getTitle());
        existingMovie.setReleaseYear(movieDTO.getReleaseYear());
        existingMovie.setMovieId(uuid);
        existingMovie.setDescription(movieDTO.getDescription());
        return movieRepository.save(existingMovie);
    }

    public void deleteMovie(UUID uuid) {
        movieRepository.deleteById(uuid);
    }

    public Movie getMovieById(UUID uuid) throws CompileTimeException {
        return movieRepository.findById(uuid).orElseThrow(
                () -> new CompileTimeException("Movie with uuid " + uuid + " not found"));
    }

    public List<Movie> getMoviesByYear(Date year) {
        return movieRepository.findByReleaseYear(year);
    }

    @Transactional
    public Movie addGenreToMovie(UUID movieUUID, List<UUID> genreUUIDs) throws CompileTimeException {
        Movie movie = movieRepository.findById(movieUUID).orElseThrow(
                () -> new CompileTimeException("Movie with uuid " + movieUUID + " not found"));

        List<MovieGenre> movieGenres = new ArrayList<>();
        movieGenreRepository.deleteByMovie_MovieId(movieUUID);


        genreUUIDs.forEach(genreUUID ->{
            Genre genre = null;
            try {
                genre = genreRepository.findById(genreUUID).orElseThrow(
                        () -> new CompileTimeException("Genre with uuid " + genreUUID + " not found"));
            } catch (CompileTimeException e) {
                throw new RuntimeException(e);
            }

            MovieGenre movieGenre = new MovieGenre();
            movieGenre.setMovie(movie);
            movieGenre.setGenre(genre);
            movieGenres.add(movieGenre);
            movieGenreRepository.save(movieGenre);
        });

        movie.getMovieGenres().clear();
        movie.getMovieGenres().addAll(movieGenres);

        return movieRepository.save(movie);
    }


    public List<Movie> getMoviesByTitle(String title) {
        return movieRepository.findByTitleStartsWith(title);
    }

    private List<String> getGenreTitles(Movie movie) {
        List<String> genreTitles = new ArrayList<>();

        movie.getMovieGenres().forEach(movieGenre -> {
            genreTitles.add(movieGenre.getGenre().getTitle());
        });

        return genreTitles;
    }

    public List<Movie> getMoviesByGenre(List<String> genres) {
        List <Movie> movies = movieRepository.findAll();
        List<Movie> result = new ArrayList<>();

        genres.forEach(genre -> {
            List<Movie> foundMovies= movies.stream().filter(movie ->
                    getGenreTitles(movie).contains(genre)).toList();

            foundMovies.forEach(movie -> {
                if (!result.contains(movie)) {
                    result.add(movie);
                }
            });
        });

        return result;

    }

    public List<Movie> getMoviesByYearRange(Date startDate, Date endDate) {
        return movieRepository.findByReleaseYearBetween(startDate, endDate);
    }

    @Transactional
    public Movie addRoleToMovie(UUID movieUUID, UUID personUUID, String roleTitle) throws CompileTimeException {
        Movie movie = movieRepository.findById(movieUUID).orElseThrow(
                () -> new CompileTimeException("Movie with uuid " + movieUUID + " not found"));

        Person person = personRepository.findByPersonId(personUUID).orElseThrow(
                () -> new IllegalStateException("Person with uuid " + personUUID + " not found"));

        Role role = new Role();
        role.setRoleName(roleTitle);

        roleRepository.save(role);

        MovieCast movieCast = new MovieCast();
        movieCast.setMovie(movie);
        movieCast.setPerson(person);
        movieCast.setRole(role);

        movieCastRepository.save(movieCast);

        movie.getMovieCastList().add(movieCast);
        return movieRepository.save(movie);
    }

    public List<MovieRoleDTO> getRolesByMovie(UUID movieUUID) throws CompileTimeException {
        Movie movie = movieRepository.findById(movieUUID).orElseThrow(
                () -> new CompileTimeException("Movie with uuid " + movieUUID + " not found"));

        List<MovieRoleDTO> movieRoleDTOs = new ArrayList<>();
        movie.getMovieCastList().forEach(movieCast -> {
            MovieRoleDTO movieRoleDTO = new MovieRoleDTO();
            movieRoleDTO.setMovieId(movie.getMovieId());
            movieRoleDTO.setMovieName(movie.getTitle());
            movieRoleDTO.setMovieCastId(movieCast.getCastId());
            movieRoleDTO.setRoleName(movieCast.getRole().getRoleName());
            movieRoleDTO.setActorName(movieCast.getPerson().getFirstName() + " " + movieCast.getPerson().getLastName());
            movieRoleDTOs.add(movieRoleDTO);
        });

        return movieRoleDTOs;
    }

    public void removeRoleFromMovie(UUID movieCastUUID) throws CompileTimeException {
        MovieCast movieCast = movieCastRepository.findById(movieCastUUID).orElseThrow(
                () -> new CompileTimeException("Movie with uuid " + movieCastUUID + " not found"));


        movieCastRepository.delete(movieCast);
    }

    public MovieCast editRoleInMovie(UUID movieCastUUID, String roleTitle) throws CompileTimeException {
        MovieCast movieCast = movieCastRepository.findById(movieCastUUID).orElseThrow(
                () -> new CompileTimeException("Movie with uuid " + movieCastUUID + " not found"));

        Role role = movieCast.getRole();
        role.setRoleName(roleTitle);

        roleRepository.save(role);
        return movieCastRepository.save(movieCast);
    }

    @Transactional
    public String uploadImage(UUID movieUUID,  MultipartFile file) throws CompileTimeException, IOException {

        Movie movie = movieRepository.findById(movieUUID).orElseThrow(
                () -> new CompileTimeException("Movie with uuid " + movieUUID + " not found"));

        // Create a temporary file
        File tempFile = File.createTempFile("upload_", file.getOriginalFilename());

        // Transfer MultipartFile content to temp file
        file.transferTo(tempFile);

        // Upload file to S3
        String s3Url = s3Service.uploadFile(tempFile);

        // Delete temp file after upload
        tempFile.delete();

        movie.setImageUrl(s3Url);
        movieRepository.save(movie);
        return s3Url;
    }

    public MovieDetails getMovieDetails(UUID uuid) throws CompileTimeException {
        Movie movie = movieRepository.findById(uuid).orElseThrow(
                () -> new CompileTimeException("Movie with uuid " + uuid + " not found"));

        MovieDetails movieDetails = new MovieDetails();
        movieDetails.setMovieId(movie.getMovieId());
        movieDetails.setTitle(movie.getTitle());
        movieDetails.setYear(movie.getReleaseYear());
        movieDetails.setDescription(movie.getDescription());
        movieDetails.setImageUrl(movie.getImageUrl());

        movieDetails.setCriticReviews(MovieDetailsUtils.getCriticReviews(movie.getReviews()));
        movieDetails.setUserReviews(MovieDetailsUtils.getUserReviews(movie.getReviews()));

        movieDetails.setMeanCriticRating(MovieDetailsUtils.getMeanCriticRating(movie.getReviews()));
        movieDetails.setMeanRating(MovieDetailsUtils.getMeanRating(movie.getReviews()));
        movieDetails.setMeanUserRating(MovieDetailsUtils.getMeanUserRating(movie.getReviews()));

        movieDetails.setAverageRating(MovieDetailsUtils.getAverageRating(movie.getReviews()));
        movieDetails.setAverageUserRating(MovieDetailsUtils.getAverageUserRating(movie.getReviews()));
        movieDetails.setAverageCriticRating(MovieDetailsUtils.getAverageCriticRating(movie.getReviews()));

        movieDetails.setActorsAndRoles(MovieDetailsUtils.getActorRoles(movie));
        movieDetails.setDirector(MovieDetailsUtils.getDirector(movie));
        movieDetails.setGenre(MovieDetailsUtils.getGenres(movie));


        return movieDetails;
    }


    public List<Movie> filterMovies(@RequestBody MovieFilers movieFilers) {

        List<Movie> movies = movieRepository.findAll();
        List<Movie> filteredMovies = new ArrayList<>();

        movies.forEach(movie -> {
            boolean matches = true;

            if (movieFilers.getTitle().isPresent() &&
                    !movie.getTitle().toLowerCase().contains(movieFilers.getTitle().get().toLowerCase())) {
                matches = false;
            }

            if (movieFilers.getEndDate().isPresent() && movie.getReleaseYear()!=null &&
                    !movie.getReleaseYear().before(movieFilers.getEndDate().get())) {
                matches = false;
            }

            if (movieFilers.getStartDate().isPresent() && movie.getReleaseYear()!=null &&
                    !movie.getReleaseYear().after(movieFilers.getStartDate().get())) {
                matches = false;
            }

            if (movieFilers.getGenresNames().isPresent()) {
                List<String> genresNames = movieFilers.getGenresNames().get();
                if (!genresNames.isEmpty() && !movie.getMovieGenres().stream()
                        .anyMatch(genre -> genresNames.contains(genre.getGenre().getTitle()))) {
                    matches = false;
                }
            }

            if (matches) {
                filteredMovies.add(movie);
            }
        });

        return filteredMovies;
    }


    public Review changeReviewStatus(UUID reviewUUID, ReviewStatus reviewStatus) throws CompileTimeException {
        Review review = reviewRepository.findById(reviewUUID).orElseThrow(
                () -> new CompileTimeException("Movie with uuid " + reviewUUID + " not found"));

        review.setStatus(reviewStatus);
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByStatus(ReviewStatus reviewStatus) {

        return reviewRepository.getReviewsByStatus(reviewStatus);
    }


}
