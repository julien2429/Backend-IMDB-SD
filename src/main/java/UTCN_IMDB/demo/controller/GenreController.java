package UTCN_IMDB.demo.controller;

import UTCN_IMDB.demo.model.Genre;
import UTCN_IMDB.demo.model.User;
import UTCN_IMDB.demo.service.GenreService;
import UTCN_IMDB.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@AllArgsConstructor
@CrossOrigin

public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genre")
    public List<Genre> getGenres() {
        return genreService.getGenres();
    }

    @PostMapping("/genre")
    public Genre addGenre(@RequestBody Genre genre) {
        return genreService.addGenre(genre);
    }

    @PutMapping("/genre/{uuid}")
    public Genre updateGenre(@PathVariable UUID uuid, @RequestBody Genre genre) {
        return genreService.updateGenre(uuid, genre);
    }

    @DeleteMapping("/genre/{uuid}")
    public void deleteGenre(@PathVariable UUID uuid) {
        genreService.deleteGenre(uuid);
    }

    @GetMapping("/genre/title/{title}")
    public Genre getGenreByTitle(@PathVariable String title) {
        return genreService.getGenreByTitle(title);
    }

    @GetMapping("/genre/{uuid}")
    public Genre getGenreById(@PathVariable UUID uuid) {
        return genreService.getGenreById(uuid);
    }


}
