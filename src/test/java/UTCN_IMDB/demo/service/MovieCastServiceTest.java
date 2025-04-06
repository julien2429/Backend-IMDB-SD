package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.model.MovieCast;
import UTCN_IMDB.demo.repository.MovieCastRepository;
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

class MovieCastServiceTest {

    @Mock
    private MovieCastRepository movieCastRepository;

    @InjectMocks
    private MovieCastService movieCastService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMovieCasts() {
        // given
        List<MovieCast> movieCasts = List.of(new MovieCast(), new MovieCast());
        when(movieCastRepository.findAll()).thenReturn(movieCasts);

        // when
        List<MovieCast> result = movieCastService.getMovieCasts();

        // then
        assertEquals(2, result.size());
        assertEquals(movieCasts, result);
        verify(movieCastRepository, times(1)).findAll();
    }

    @Test
    void testGetMovieCastById() {
        // given
        UUID uuid = UUID.randomUUID();
        MovieCast movieCast = new MovieCast();
        movieCast.setCastId(uuid);
        when(movieCastRepository.findById(uuid)).thenReturn(Optional.of(movieCast));

        // when
        MovieCast result = movieCastService.getMovieCastById(uuid);

        // then
        assertEquals(uuid, result.getCastId());
        verify(movieCastRepository, times(1)).findById(uuid);
    }

    @Test
    void testGetMovieCastByIdNotFound() {
        // given
        UUID uuid = UUID.randomUUID();
        when(movieCastRepository.findById(uuid)).thenReturn(Optional.empty());

        // when, then
        assertThrows(IllegalStateException.class, () -> movieCastService.getMovieCastById(uuid));
        verify(movieCastRepository, times(1)).findById(uuid);
    }

    @Test
    void testDeleteMovieCast() {
        // given
        UUID uuid = UUID.randomUUID();
        doNothing().when(movieCastRepository).deleteById(uuid);

        // when
        movieCastService.deleteMovieCast(uuid);

        // then
        verify(movieCastRepository, times(1)).deleteById(uuid);
    }

    @Test
    void testAddMovieCast() {
        // given
        MovieCast movieCastToSave = new MovieCast();
        MovieCast savedMovieCast = new MovieCast();
        savedMovieCast.setCastId(UUID.randomUUID());

        when(movieCastRepository.save(movieCastToSave)).thenReturn(savedMovieCast);

        // when
        MovieCast result = movieCastService.addMovieCast(movieCastToSave);

        // then
        assertNotNull(result.getCastId());
        verify(movieCastRepository, times(1)).save(movieCastToSave);
    }

    @Test
    void testUpdateMovieCast() {
        // given
        UUID uuid = UUID.randomUUID();
        MovieCast existingMovieCast = new MovieCast();
        existingMovieCast.setCastId(uuid);

        MovieCast movieCastUpdate = new MovieCast();
        // Set update properties as needed
        //movieCastUpdate.setMovie(...);
        //movieCastUpdate.setPerson(...);
        //movieCastUpdate.setRole(...);

        MovieCast updatedMovieCast = new MovieCast();
        updatedMovieCast.setCastId(uuid);
        // Set updated properties

        when(movieCastRepository.findById(uuid)).thenReturn(Optional.of(existingMovieCast));
        when(movieCastRepository.save(any(MovieCast.class))).thenReturn(updatedMovieCast);

        // when
        MovieCast result = movieCastService.updateMovieCast(uuid, movieCastUpdate);

        // then
        assertNotNull(result.getCastId());
        verify(movieCastRepository, times(1)).findById(uuid);
        verify(movieCastRepository, times(1)).save(any(MovieCast.class));
    }

    @Test
    void testUpdateMovieCastNotFound() {
        // given
        UUID uuid = UUID.randomUUID();
        MovieCast movieCastUpdate = new MovieCast();

        when(movieCastRepository.findById(uuid)).thenReturn(Optional.empty());

        // when, then
        assertThrows(IllegalStateException.class, () -> movieCastService.updateMovieCast(uuid, movieCastUpdate));
        verify(movieCastRepository, times(1)).findById(uuid);
        verify(movieCastRepository, times(0)).save(any(MovieCast.class));
    }
}