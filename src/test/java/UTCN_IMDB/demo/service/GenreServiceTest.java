package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.DTO.GenreDTO;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.model.Genre;
import UTCN_IMDB.demo.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetGenres() {
        // given
        List<Genre> genres = List.of(new Genre(), new Genre());
        when(genreRepository.findAll()).thenReturn(genres);

        // when
        List<Genre> result = genreService.getGenres();

        // then
        assertEquals(2, result.size());
        assertEquals(genres, result);
        verify(genreRepository, times(1)).findAll();
    }

    @Test
    void testAddGenre() throws CompileTimeException {
        // given
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setTitle("Action");
        Genre genreToSave = new Genre();
        genreToSave.setTitle(genreDTO.getTitle());
        Genre savedGenre = new Genre();
        savedGenre.setGenreId(UUID.randomUUID());
        savedGenre.setTitle(genreToSave.getTitle());

        when(genreRepository.findByTitleIgnoreCase(genreDTO.getTitle())).thenReturn(Optional.empty());
        when(genreRepository.save(any(Genre.class))).thenReturn(savedGenre);

        // when
        Genre result = genreService.addGenre(genreDTO);

        // then
        assertNotNull(result.getGenreId());
        assertEquals(savedGenre.getTitle(), result.getTitle());
        verify(genreRepository, times(1)).findByTitleIgnoreCase(genreDTO.getTitle());
        verify(genreRepository, times(1)).save(genreToSave);
    }

    @Test
    void testAddGenreAlreadyExists() {
        // given
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setTitle("Action");
        Genre existingGenre = new Genre();
        existingGenre.setGenreId(UUID.randomUUID());
        existingGenre.setTitle(genreDTO.getTitle());

        when(genreRepository.findByTitleIgnoreCase(genreDTO.getTitle())).thenReturn(Optional.of(existingGenre));

        // when, then
        assertThrows(CompileTimeException.class, () -> genreService.addGenre(genreDTO));
        verify(genreRepository, times(1)).findByTitleIgnoreCase(genreDTO.getTitle());
        verify(genreRepository, times(0)).save(any(Genre.class));
    }

    @Test
    void testUpdateGenre() throws CompileTimeException {
        // given
        UUID uuid = UUID.randomUUID();
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setTitle("Comedy");

        Genre existingGenre = new Genre();
        existingGenre.setGenreId(uuid);
        existingGenre.setTitle("Action");

        Genre updatedGenre = new Genre();
        updatedGenre.setGenreId(uuid);
        updatedGenre.setTitle(genreDTO.getTitle());

        when(genreRepository.findById(uuid)).thenReturn(Optional.of(existingGenre));
        when(genreRepository.findByTitleIgnoreCase(genreDTO.getTitle())).thenReturn(Optional.empty());
        when(genreRepository.save(any(Genre.class))).thenReturn(updatedGenre);

        // when
        Genre result = genreService.updateGenre(uuid, genreDTO);

        // then
        assertEquals(genreDTO.getTitle(), result.getTitle());
        verify(genreRepository, times(1)).findById(uuid);
        verify(genreRepository, times(1)).findByTitleIgnoreCase(genreDTO.getTitle());
        verify(genreRepository, times(1)).save(any(Genre.class));
    }

    @Test
    void testUpdateGenreNotFound() {
        // given
        UUID uuid = UUID.randomUUID();
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setTitle("Comedy");

        when(genreRepository.findById(uuid)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompileTimeException.class, () -> genreService.updateGenre(uuid, genreDTO));
        verify(genreRepository, times(1)).findById(uuid);
        verify(genreRepository, times(0)).findByTitleIgnoreCase(anyString());
        verify(genreRepository, times(0)).save(any(Genre.class));
    }

    @Test
    void testUpdateGenreAlreadyExists() {
        // given
        UUID uuid = UUID.randomUUID();
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setTitle("Comedy");

        Genre existingGenre = new Genre();
        existingGenre.setGenreId(uuid);
        existingGenre.setTitle("Action");

        Genre conflictingGenre = new Genre();
        conflictingGenre.setGenreId(UUID.randomUUID()); // Different UUID
        conflictingGenre.setTitle(genreDTO.getTitle());

        when(genreRepository.findById(uuid)).thenReturn(Optional.of(existingGenre));
        when(genreRepository.findByTitleIgnoreCase(genreDTO.getTitle())).thenReturn(Optional.of(conflictingGenre));

        // when, then
        assertThrows(CompileTimeException.class, () -> genreService.updateGenre(uuid, genreDTO));
        verify(genreRepository, times(1)).findById(uuid);
        verify(genreRepository, times(2)).findByTitleIgnoreCase(genreDTO.getTitle());
        verify(genreRepository, times(0)).save(any(Genre.class));
    }

    @Test
    void testDeleteGenre() {
        // given
        UUID uuid = UUID.randomUUID();
        doNothing().when(genreRepository).deleteById(uuid);

        // when
        genreService.deleteGenre(uuid);

        // then
        verify(genreRepository, times(1)).deleteById(uuid);
    }

    @Test
    void testGetGenreByTitle() throws CompileTimeException {
        // given
        String title = "Action";
        Genre genre = new Genre();
        genre.setGenreId(UUID.randomUUID());
        genre.setTitle(title);

        when(genreRepository.findByTitle(title)).thenReturn(Optional.of(genre));

        // when
        Genre result = genreService.getGenreByTitle(title);

        // then
        assertEquals(title, result.getTitle());
        verify(genreRepository, times(1)).findByTitle(title);
    }

    @Test
    void testGetGenreByTitleNotFound() {
        // given
        String title = "Action";
        when(genreRepository.findByTitle(title)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompileTimeException.class, () -> genreService.getGenreByTitle(title));
        verify(genreRepository, times(1)).findByTitle(title);
    }

    @Test
    void testGetGenreById() throws CompileTimeException {
        // given
        UUID uuid = UUID.randomUUID();
        Genre genre = new Genre();
        genre.setGenreId(uuid);
        genre.setTitle("Action");

        when(genreRepository.findById(uuid)).thenReturn(Optional.of(genre));

        // when
        Genre result = genreService.getGenreById(uuid);

        // then
        assertEquals(uuid, result.getGenreId());
        verify(genreRepository, times(1)).findById(uuid);
    }

    @Test
    void testGetGenreByIdNotFound() {
        // given
        UUID uuid = UUID.randomUUID();
        when(genreRepository.findById(uuid)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompileTimeException.class, () -> genreService.getGenreById(uuid));
        verify(genreRepository, times(1)).findById(uuid);
    }
}