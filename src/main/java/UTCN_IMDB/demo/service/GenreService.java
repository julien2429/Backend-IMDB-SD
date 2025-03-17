package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.DTO.GenreDTO;
import UTCN_IMDB.demo.config.CompileTimeException;
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

    public Genre addGenre(GenreDTO genreDTO) throws CompileTimeException {

        if(genreRepository.findByTitleIgnoreCase(genreDTO.getTitle()).isPresent())
        {
            throw new CompileTimeException("Genre with title " + genreDTO.getTitle() + " already exists");
        }

        Genre genre = new Genre();
        genre.setTitle(genreDTO.getTitle());
        return genreRepository.save( genre);
    }

    public Genre updateGenre(UUID uuid, GenreDTO genreDTO) throws  CompileTimeException{
        Genre existingGenre =
                genreRepository.findById(uuid).orElseThrow(
                        () -> new CompileTimeException("Genre with uuid " + uuid + " not found"));

        if(genreRepository.findByTitleIgnoreCase(genreDTO.getTitle()).isPresent() && !genreRepository.findByTitleIgnoreCase(genreDTO.getTitle()).get().getGenreId().equals(uuid))
        {
            throw new CompileTimeException("Genre with title " + genreDTO.getTitle() + " already exists");
        }

        existingGenre.setTitle(genreDTO.getTitle());
        return genreRepository.save(existingGenre);
    }

    public void deleteGenre(UUID uuid) {
        genreRepository.deleteById(uuid);
    }

    public Genre getGenreByTitle(String title) throws CompileTimeException{
        return genreRepository.findByTitle(title).orElseThrow(
                () -> new CompileTimeException("Genre with title " + title + " not found"));
    }

    public Genre getGenreById(UUID uuid) throws CompileTimeException{
        return genreRepository.findById(uuid).orElseThrow(
                () -> new CompileTimeException("Genre with id " + uuid + " not found"));
    }


}
