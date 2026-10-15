package acsanfrancisco.cine_review.service;

import acsanfrancisco.cine_review.dto.response.MovieResponse;
import acsanfrancisco.cine_review.entity.Movie;
import acsanfrancisco.cine_review.exception.ResourceAlreadyExistsException;
import acsanfrancisco.cine_review.exception.ResourceNotFoundException;
import acsanfrancisco.cine_review.integration.MovieSearch;
import acsanfrancisco.cine_review.integration.tmdb.dto.TmdbMovieResponse;
import acsanfrancisco.cine_review.integration.tmdb.dto.TmdbSearchResponse;
import acsanfrancisco.cine_review.repository.MovieRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TmdbMovieServiceTest {

    @Mock
    MovieRepository movieRepository;

    @Mock
    MovieSearch<TmdbSearchResponse, TmdbMovieResponse> movieSearch;

    @InjectMocks
    TmdbMovieService tmdbMovieService;

    TmdbMovieResponse tmdbMovie1;

    TmdbMovieResponse tmdbMovie2;

    MovieResponse movie1;

    Movie movie;

    @BeforeEach
    void setUp(){
        tmdbMovie1 = new TmdbMovieResponse();
        tmdbMovie1.setId(100L);
        tmdbMovie1.setTitle("Batman");
        tmdbMovie1.setReleaseDate(LocalDate.of(1989, 6, 23));
        tmdbMovie1.setOriginalLanguage("en");
        tmdbMovie1.setOriginalTitle("Batman");

        tmdbMovie2 = new TmdbMovieResponse();
        tmdbMovie2.setId(200L);
        tmdbMovie2.setTitle("Batman Begins");
        tmdbMovie2.setReleaseDate(LocalDate.of(2005, 6, 15));
        tmdbMovie2.setOriginalLanguage("en");
        tmdbMovie2.setOriginalTitle("Batman Begins");

        movie1 = new MovieResponse();
        movie1.setId(1L);
        movie1.setExternalId(100L);
        movie1.setTitle("Batman");
        movie1.setReleaseDate(LocalDate.of(1989, 6, 23));
        movie1.setOriginalLanguage("en");
        movie1.setOriginalTitle("Batman");

        movie = new Movie();
        movie.setId(1L);
        movie.setExternalId(100L);
        movie.setTitle("Batman");
        movie.setReleaseDate(LocalDate.of(1989, 6, 23));
        movie.setOriginalLanguage("en");
        movie.setOriginalTitle("Batman");
    }

    @Test
    void shouldFindMoviesByTitle(){
        TmdbSearchResponse tmdbSearchResponse = new TmdbSearchResponse();
        tmdbSearchResponse.setResults(List.of(tmdbMovie1, tmdbMovie2));

        Mockito.when(movieSearch.searchMovieByTitle("Batman")).thenReturn(tmdbSearchResponse);
        List<MovieResponse> movieResponseList = tmdbMovieService.findMoviesByTitle("Batman");
        Assertions.assertEquals(2, movieResponseList.size());
    }

    @Test
    void shouldReturnAEmptyListWhenTitleIsNotFound(){
        TmdbSearchResponse tmdbSearchResponse = new TmdbSearchResponse();
        tmdbSearchResponse.setResults(List.of());

        Mockito.when(movieSearch.searchMovieByTitle("Title not found")).thenReturn(tmdbSearchResponse);
        List<MovieResponse> movieResponseList = tmdbMovieService.findMoviesByTitle("Title not found");
        Assertions.assertNotNull(movieResponseList);
        Assertions.assertTrue(movieResponseList.isEmpty());
        Mockito.verify(movieSearch).searchMovieByTitle("Title not found");
    }

    @Test
    void shouldSaveMovie() {
        Mockito.when(movieSearch.searchMovieByExternalId(100L)).thenReturn(tmdbMovie1);
        Mockito.when(movieRepository.save(Mockito.any(Movie.class))).thenAnswer(i -> i.getArgument(0));
        Mockito.when(movieRepository.existsByExternalId(100L)).thenReturn(false);
        MovieResponse savedMovie = tmdbMovieService.saveMovie(100L);

        Mockito.verify(movieRepository).save(Mockito.any(Movie.class));
        Mockito.verify(movieSearch).searchMovieByExternalId(100L);
        Mockito.verify(movieRepository).existsByExternalId(100L);

        Assertions.assertEquals(tmdbMovie1.getId(), savedMovie.getExternalId());
        Assertions.assertEquals(tmdbMovie1.getTitle(), savedMovie.getTitle());
        Assertions.assertEquals(tmdbMovie1.getReleaseDate(), savedMovie.getReleaseDate());
        Assertions.assertEquals(tmdbMovie1.getOriginalLanguage(), savedMovie.getOriginalLanguage());
        Assertions.assertEquals(tmdbMovie1.getOriginalTitle(), savedMovie.getOriginalTitle());
    }

    @Test
    void shouldNotSaveMovieWhenMovieIsNotFound() {
        Mockito.when(movieSearch.searchMovieByExternalId(100L)).thenReturn(null);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            tmdbMovieService.saveMovie(100L);
        });
        Mockito.verify(movieSearch).searchMovieByExternalId(100L);
        Mockito.verify(movieRepository, Mockito.never()).save(Mockito.any(Movie.class));
    }

    @Test
    void shouldNotSaveMovieWhenExternalIdAlreadyExists() {
        Mockito.when(movieSearch.searchMovieByExternalId(200L)).thenReturn(tmdbMovie2);
        Mockito.when(movieRepository.existsByExternalId(200L)).thenReturn(true);
        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> {
            tmdbMovieService.saveMovie(200L);
        });
        Mockito.verify(movieRepository, Mockito.never()).save(Mockito.any(Movie.class));
        Mockito.verify(movieSearch).searchMovieByExternalId(200L);
        Mockito.verify(movieRepository).existsByExternalId(200L);
    }
}
