package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.model.Genre;
import UTCN_IMDB.demo.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }

    public Genre addGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    public Genre updateGenre(UUID uuid, Genre genre) {
        Genre existingGenre =
                genreRepository.findById(uuid).orElseThrow(
                        () -> new IllegalStateException("Genre with uuid " + uuid + " not found"));
        existingGenre.setTitle(genre.getTitle());
        existingGenre.setGenreId(genre.getGenreId());

        return genreRepository.save(existingGenre);
    }

    public void deleteGenre(UUID uuid) {
        genreRepository.deleteById(uuid);
    }

    public Genre getGenreByTitle(String title) {
        return genreRepository.findByTitle(title).orElseThrow(
                () -> new IllegalStateException("Genre with title " + title + " not found"));
    }

    public Genre getGenreById(UUID uuid) {
        return genreRepository.findById(uuid).orElseThrow(
                () -> new IllegalStateException("Genre with id " + uuid + " not found"));
    }


}
