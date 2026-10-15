package acsanfrancisco.cine_review.service;

import acsanfrancisco.cine_review.entity.Movie;
import acsanfrancisco.cine_review.exception.ResourceNotFoundException;
import acsanfrancisco.cine_review.repository.MovieRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    MovieRepository movieRepository;

    @InjectMocks
    MovieService movieService;

    @Test
    @DisplayName("Should return a Movie by external ID")
    void shouldFindSavedMovieByExternalId(){
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setExternalId(100L);
        movie.setTitle("The Matrix");
        movie.setReleaseDate(LocalDate.of(1999,3,31));
        movie.setOriginalLanguage("en");
        movie.setOriginalTitle("The Matrix");

        Mockito.when(movieRepository.findByExternalId(movie.getExternalId())).thenReturn(Optional.of(movie));
        Movie foundMovie = movieService.findMovieByExternalId(movie.getExternalId());
        Mockito.verify(movieRepository).findByExternalId(movie.getExternalId());

        Assertions.assertEquals(movie.getId(), foundMovie.getId());
        Assertions.assertEquals(movie.getExternalId(), foundMovie.getExternalId());
        Assertions.assertEquals(movie.getTitle(), foundMovie.getTitle());
        Assertions.assertEquals(movie.getReleaseDate(), foundMovie.getReleaseDate());
        Assertions.assertEquals(movie.getOriginalLanguage(), foundMovie.getOriginalLanguage());
        Assertions.assertEquals(movie.getOriginalTitle(), foundMovie.getOriginalTitle());
    }

    @Test
    @DisplayName("Should throw Resource Not Found Exception")
    void shouldNotFindSavedMovieWithNonExistentExternalId(){
        Mockito.when(movieRepository.findByExternalId(100L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            movieService.findMovieByExternalId(100L);
        });
        Mockito.verify(movieRepository).findByExternalId(100L);
    }

    @Test
    @DisplayName("Should return a Movie by ID")
    void shouldFindMovieById(){
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setExternalId(100L);
        movie.setTitle("The Matrix");
        movie.setReleaseDate(LocalDate.of(1999,3,31));
        movie.setOriginalLanguage("en");
        movie.setOriginalTitle("The Matrix");

        Mockito.when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
        Movie foundMovie = movieService.findMovieById(movie.getId());
        Mockito.verify(movieRepository).findById(movie.getId());

        Assertions.assertEquals(movie.getId(), foundMovie.getId());
        Assertions.assertEquals(movie.getExternalId(), foundMovie.getExternalId());
        Assertions.assertEquals(movie.getTitle(), foundMovie.getTitle());
        Assertions.assertEquals(movie.getReleaseDate(), foundMovie.getReleaseDate());
        Assertions.assertEquals(movie.getOriginalLanguage(), foundMovie.getOriginalLanguage());
        Assertions.assertEquals(movie.getOriginalTitle(), foundMovie.getOriginalTitle());
    }

    @Test
    @DisplayName("Should throw Resource Not Found Exception")
    void shouldNotFindMovieWithNonExistentExternalId(){
        Mockito.when(movieRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            movieService.findMovieById(1L);
        });
        Mockito.verify(movieRepository).findById(1L);
    }
}
