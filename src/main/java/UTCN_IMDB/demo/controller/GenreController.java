package UTCN_IMDB.demo.controller;

import UTCN_IMDB.demo.DTO.GenreDTO;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.model.Genre;
import UTCN_IMDB.demo.model.User;
import UTCN_IMDB.demo.service.GenreService;
import UTCN_IMDB.demo.service.UserService;
import jakarta.validation.Valid;
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
    public Genre addGenre(@RequestBody @Valid GenreDTO genreDTO) throws CompileTimeException {
        return genreService.addGenre(genreDTO);
    }

    @PutMapping("/genre/{uuid}")
    public Genre updateGenre(@PathVariable UUID uuid, @RequestBody @Valid GenreDTO genreDTO) throws CompileTimeException {
        return genreService.updateGenre(uuid, genreDTO);
    }

    @DeleteMapping("/genre/{uuid}")
    public void deleteGenre(@PathVariable UUID uuid) {
        genreService.deleteGenre(uuid);
    }

    @GetMapping("/genre/title/{title}")
    public Genre getGenreByTitle(@PathVariable String title) throws CompileTimeException {
        return genreService.getGenreByTitle(title);
    }

    @GetMapping("/genre/{uuid}")
    public Genre getGenreById(@PathVariable UUID uuid) throws CompileTimeException {
        return genreService.getGenreById(uuid);
    }


}
