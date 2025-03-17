package UTCN_IMDB.demo.controller;


import UTCN_IMDB.demo.DTO.MovieDTO;
import UTCN_IMDB.demo.DTO.RoleDTO;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.model.Movie;
import UTCN_IMDB.demo.model.Person;
import UTCN_IMDB.demo.model.Role;
import UTCN_IMDB.demo.service.MovieService;
import jakarta.persistence.Tuple;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.web.bind.annotation.*;

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
    public Movie getMovieById(@PathVariable UUID uuid) {
        return movieService.getMovieById(uuid);
    }

    @PostMapping("/movie")
    public Movie addMovie(@RequestBody @Valid MovieDTO movieDTO) {
        return movieService.addMovie(movieDTO);
    }

    @PutMapping("/movie/{uuid}")
    public Movie updateMovie(@RequestBody @Valid MovieDTO movieDTO, @PathVariable UUID uuid) {
        return movieService.updateMovie(uuid, movieDTO);
    }

    @PutMapping("/movie/addGenre/{movieUUID}")
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


}
