package acsanfrancisco.cine_review.controller;

import acsanfrancisco.cine_review.dto.response.MovieResponse;
import acsanfrancisco.cine_review.entity.Movie;
import acsanfrancisco.cine_review.exception.ResourceAlreadyExistsException;
import acsanfrancisco.cine_review.exception.ResourceNotFoundException;
import acsanfrancisco.cine_review.service.MovieService;
import acsanfrancisco.cine_review.service.TmdbMovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;
import java.util.List;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @MockitoBean
    TmdbMovieService tmdbMovieService;

    @MockitoBean
    MovieService movieService;

    @Autowired
    MockMvc mockMvc;

    MovieResponse movieResponse;

    Movie movie;

    @BeforeEach
    void setUp(){
        movieResponse = new MovieResponse();
        movieResponse.setId(1L);
        movieResponse.setTitle("The Batman");
        movieResponse.setOriginalTitle("The Batman");
        movieResponse.setExternalId(100L);
        movieResponse.setReleaseDate(LocalDate.of(2008,7,18));
        movieResponse.setOriginalLanguage("en");

        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("The Batman");
        movie.setOriginalTitle("The Batman");
        movie.setExternalId(100L);
        movie.setReleaseDate(LocalDate.of(2008,7,18));
        movie.setOriginalLanguage("en");
    }

    @Test
    void shouldFindMoviesByTitle() throws Exception {
        Mockito.when(tmdbMovieService.findMoviesByTitle("The Batman"))
                .thenReturn(List.of(movieResponse));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("title", movieResponse.getTitle())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(movieResponse.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(movieResponse.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].originalTitle").value(movieResponse.getOriginalTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].externalId").value(movieResponse.getExternalId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseDate").value(movieResponse.getReleaseDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].originalLanguage").value(movieResponse.getOriginalLanguage()));
    }

    @Test
    void shouldReturnEmptyListWhenFindMoviesByTitle() throws Exception {
        Mockito.when(tmdbMovieService.findMoviesByTitle("The Batman"))
                .thenReturn(List.of());

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("title", movieResponse.getTitle())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

    @Test
    void shouldSaveMovie() throws Exception {
        Mockito.when(tmdbMovieService.saveMovie(Mockito.any(Long.class)))
                .thenReturn(movieResponse);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("externalId", movieResponse.getExternalId().toString())
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(movieResponse.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(movieResponse.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.originalTitle").value(movieResponse.getOriginalTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.externalId").value(movieResponse.getExternalId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value(movieResponse.getReleaseDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.originalLanguage").value(movieResponse.getOriginalLanguage()));
    }

    @Test
    void shouldNotSaveMovieWhenMovieIsNotFound() throws Exception {
        Mockito.when(tmdbMovieService.saveMovie(Mockito.any(Long.class)))
                .thenThrow(new ResourceNotFoundException("Movie not found for external ID"));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("externalId", movieResponse.getExternalId().toString())
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Movie not found for external ID"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.method").value("POST"));
    }

    @Test
    void shouldNotSaveMovieWhenExternalIdExists() throws Exception {
        Mockito.when(tmdbMovieService.saveMovie(Mockito.any(Long.class)))
                .thenThrow(new ResourceAlreadyExistsException("Movie already exists for external ID"));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("externalId", movieResponse.getExternalId().toString())
        )
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Movie already exists for external ID"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("409"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.method").value("POST"));
    }

    @Test
    void shouldFindMovieByExternalId() throws Exception {
        Mockito.when(movieService.findMovieByExternalId(Mockito.any(Long.class)))
                .thenReturn(movie);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/movies/external/" + movie.getExternalId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(movie.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(movie.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.originalTitle").value(movie.getOriginalTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.externalId").value(movie.getExternalId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value(movie.getReleaseDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.originalLanguage").value(movie.getOriginalLanguage()));
    }

    @Test
    void shouldNotFindMovieByExternalIdWhenMovieIsNotFound() throws Exception {
        Mockito.when(movieService.findMovieByExternalId(Mockito.any(Long.class)))
                .thenThrow(new ResourceNotFoundException("Movie not found for external ID"));

        mockMvc.perform(
                MockMvcRequestBuilders.
                        get("/api/v1/movies/external/" + movie.getExternalId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Movie not found for external ID"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.method").value("GET"));
    }

    @Test
    void shouldFindMovieById() throws Exception {
        Mockito.when(movieService.findMovieById(Mockito.any(Long.class)))
                .thenReturn(movie);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/movies/" + movie.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(movie.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(movie.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.originalTitle").value(movie.getOriginalTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.externalId").value(movie.getExternalId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value(movie.getReleaseDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.originalLanguage").value(movie.getOriginalLanguage()));
    }

    @Test
    void shouldNotFindMovieByIdWhenMovieIsNotFound() throws Exception {
        Mockito.when(movieService.findMovieById(Mockito.any(Long.class)))
                .thenThrow(new ResourceNotFoundException("Movie not found for ID"));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/movies/" + movie.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Movie not found for ID"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.method").value("GET"));
    }

}