package UTCN_IMDB.demo.controller;


import UTCN_IMDB.demo.DTO.MovieDTO;
import UTCN_IMDB.demo.config.CompileTimeException;
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

    // Upload a file to S3
    @PostMapping("/movie/uploadImage/{movieUUID}")
    public String uploadFile(@PathVariable UUID movieUUID, @RequestParam("file") MultipartFile file) throws CompileTimeException, IOException {
        return  movieService.uploadImage(movieUUID, file);
    }

    @PostMapping("/movie/filterMovies")
    public List<Movie> filterMovies(@RequestBody MovieFilers movieFilers) {
        return movieService.filterMovies(movieFilers);
    }

}
