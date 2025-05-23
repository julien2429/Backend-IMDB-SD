package UTCN_IMDB.demo.controller;


import UTCN_IMDB.demo.DTO.MovieDTO;
import UTCN_IMDB.demo.DTO.MovieRoleDTO;
import UTCN_IMDB.demo.DTO.RoleDTO;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.enums.ReviewStatus;
import UTCN_IMDB.demo.model.*;
import UTCN_IMDB.demo.service.MovieService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@CrossOrigin
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/movie")
    public List<Movie> getMovies() {
        return movieService.getMovies();
    }

    @GetMapping("/movie/title/{title}")
    public List<Movie> getMoviesByTitle(@PathVariable String title) {
        return movieService.getMoviesByTitle(title);
    }

    @GetMapping("/movie/{uuid}")
    public Movie getMovieById(@PathVariable UUID uuid) throws CompileTimeException {
        return movieService.getMovieById(uuid);
    }

    @GetMapping("/movie/details/{uuid}")
    public MovieDetails getMovieDetails(@PathVariable UUID uuid) throws CompileTimeException {
        return movieService.getMovieDetails(uuid);
    }

    @PostMapping("/movie")
    public Movie addMovie(@RequestBody @Valid MovieDTO movieDTO) {
        return movieService.addMovie(movieDTO);
    }

    @PostMapping("/movie/{uuid}")
    public Movie updateMovie(@PathVariable UUID uuid, @RequestBody @Valid MovieDTO movieDTO) throws CompileTimeException {
        return movieService.updateMovie(uuid, movieDTO);
    }

    @PostMapping("/movie/addGenre/{movieUUID}")
    public Movie addGenreToMovie(@PathVariable UUID movieUUID, @RequestBody List<UUID> genreUUIDs) throws CompileTimeException {
        return movieService.addGenreToMovie(movieUUID, genreUUIDs);
    }

    @DeleteMapping("/movie/{uuid}")
    public void deleteMovie(@PathVariable UUID uuid) {
        movieService.deleteMovie(uuid);
    }

    @GetMapping("/movie/year/{year}")
    public List<Movie> getMoviesByYear(@PathVariable Date year) {
        return movieService.getMoviesByYear(year);
    }

    @GetMapping("/movie/genre")
    public List<Movie> getMoviesByGenre(@RequestBody List<String> genres) {
        return movieService.getMoviesByGenre(genres);
    }

    @GetMapping("/movie/year/{startDate}/{endDate}")
    public List<Movie> getMoviesByYearRange(@PathVariable Date startDate, @PathVariable Date endDate) {
        return movieService.getMoviesByYearRange(startDate, endDate);
    }

    @PutMapping("/movie/addRole/{movieUUID}/{personUUID}/{roleTitle}")
    public Movie addRoleToMovie(@PathVariable UUID movieUUID, @PathVariable UUID personUUID, @PathVariable String roleTitle) throws CompileTimeException {
        return movieService.addRoleToMovie(movieUUID, personUUID, roleTitle);
    }

    @GetMapping("/movie/getRoles/{movieUUID}")
    public List<MovieRoleDTO> getRolesByMovie(@PathVariable UUID movieUUID) throws CompileTimeException {
        return movieService.getRolesByMovie(movieUUID);
    }

    @PostMapping("/movie/removeRole/{movieCastUUID}")
    public void removeRoleFromMovie(@PathVariable UUID movieCastUUID) throws CompileTimeException {
        movieService.removeRoleFromMovie(movieCastUUID);
    }

    @PostMapping("/movie/editRole/{movieCastUUID}/{roleTitle}")
    public MovieCast editRoleInMovie(@PathVariable UUID movieCastUUID, @PathVariable String roleTitle) throws CompileTimeException {
        return movieService.editRoleInMovie(movieCastUUID, roleTitle);
    }

    // Upload a file to S3
    @PostMapping("/movie/uploadImage/{movieUUID}")
    public String uploadFile(@PathVariable UUID movieUUID, @RequestParam("file") MultipartFile file) throws CompileTimeException, IOException {
        return  movieService.uploadImage(movieUUID, file);
    }

    @PostMapping("/movie/filterMovies")
    public List<Movie> filterMovies(@RequestBody MovieFilers movieFilers) {
        return movieService.filterMovies(movieFilers);
    }

    @PostMapping("/movie/approveReview/{reviewUUID}")
    public Review approveReview(@PathVariable UUID reviewUUID) throws CompileTimeException {
        return movieService.changeReviewStatus(reviewUUID, ReviewStatus.APPROVED);
    }

    @PostMapping("/movie/rejectReview/{reviewUUID}")
    public Review rejectReview(@PathVariable UUID reviewUUID) throws CompileTimeException {
        return movieService.changeReviewStatus(reviewUUID, ReviewStatus.REJECTED);
    }

    @PostMapping("/movie/setToPending/{reviewUUID}")
    public Review setToPending(@PathVariable UUID reviewUUID) throws CompileTimeException {
        return movieService.changeReviewStatus(reviewUUID, ReviewStatus.PENDING);
    }

    @GetMapping("/movie/getAllPendingReviews")
    public List<Review> getAllPendingReviews() {
        return movieService.getReviewsByStatus(ReviewStatus.PENDING);
    }

    @GetMapping("/movie/getAllApprovedReviews")
    public List<Review> getAllApprovedReviews() {
        return movieService.getReviewsByStatus(ReviewStatus.APPROVED);
    }

    @GetMapping("/movie/getAllRejectedReviews")
    public List<Review> getAllRejectedReviews() {
        return movieService.getReviewsByStatus(ReviewStatus.REJECTED);
    }


    @PostMapping("/movie/removeGenreFromMovie/{movieUUID}/{genreUUID}")
    public Movie removeGenreFromMovie(@PathVariable UUID movieUUID, @PathVariable UUID genreUUID) throws CompileTimeException {
        return movieService.removeGenreFromMovie(movieUUID, genreUUID);
    }

    @GetMapping("/movie/getGenresByMovie/{movieUUID}")
    public List<Genre> getGenresByMovie(@PathVariable UUID movieUUID) throws CompileTimeException {
        return movieService.getGenresByMovie(movieUUID);
    }

}
