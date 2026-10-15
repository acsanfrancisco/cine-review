package acsanfrancisco.cine_review.integration.tmdb.service;

import acsanfrancisco.cine_review.exception.IntegrationException;
import acsanfrancisco.cine_review.integration.tmdb.dto.TmdbMovieResponse;
import acsanfrancisco.cine_review.integration.tmdb.dto.TmdbSearchResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class TmdbServiceTest {

    MockRestServiceServer mockServer;

    TmdbService tmdbService;

    @BeforeEach
    void setUp(){
        RestClient.Builder builder = RestClient.builder();

        mockServer = MockRestServiceServer.bindTo(builder).build();

        RestClient restClient = builder.baseUrl("https://api.themoviedb.org/3/").build();

        tmdbService = new TmdbService(restClient);
    }


    @Test
    @DisplayName("Should return a movie by external ID")
    void shouldReturnTmdbSearchResponseByExternalId(){
        mockServer.expect(requestTo(
                "https://api.themoviedb.org/3/movie/100"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        """
                            {
                                "id": 100,
                                "title": "The Matrix",
                                "original_title": "The Matrix",
                                "original_language": "en",
                                "release_date": "1999-03-31"
                            }
                            """, MediaType.APPLICATION_JSON));

        TmdbMovieResponse response = tmdbService.searchMovieByExternalId(100L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(100, response.getId());
        Assertions.assertEquals("The Matrix", response.getTitle());

        mockServer.verify();
    }

    @Test
    @DisplayName("Should return search response")
    void shouldReturnTmdbSearchResponseByTitle(){
        mockServer.expect(requestTo(
                "https://api.themoviedb.org/3/search/movie?query=The%20Matrix"
        ))
                .andExpect(queryParam("query", "The%20Matrix"))
                .andRespond(withSuccess(
                        """
                 {
                     "results": [
                         {
                             "id": 100,
                             "title": "The Matrix",
                             "original_title": "The Matrix",
                             "original_language": "en",
                             "release_date": "1999-03-31"
                         },
                         {
                              "id": 200,
                              "title": "The Batman",
                              "original_title": "The Batman",
                              "original_language": "en",
                              "release_date": "2000-03-27"
                         }
                     ]
                 }
                 """, MediaType.APPLICATION_JSON));

        TmdbSearchResponse response = tmdbService.searchMovieByTitle("The Matrix");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.getResults().size());
        Assertions.assertEquals(100, response.getResults().getFirst().getId());
        Assertions.assertEquals(200, response.getResults().getLast().getId());
        Assertions.assertEquals("The Matrix", response.getResults().getFirst().getTitle());
        Assertions.assertEquals("The Batman", response.getResults().getLast().getOriginalTitle());

        mockServer.verify();
    }

    @Test
    @DisplayName("Should throw Integration Exception")
    void shouldThrowExceptionWhenSearchByExternalId(){
        mockServer.expect(requestTo(
                "https://api.themoviedb.org/3/movie/100"
        ))
                .andRespond(withStatus(HttpStatus.BAD_GATEWAY));

        Assertions.assertThrows(IntegrationException.class, () -> {
            tmdbService.searchMovieByExternalId(100L);
        });

        mockServer.verify();
    }

    @Test
    @DisplayName("Should throw Integration Exception")
    void shouldThrowExceptionWhenSearchByTitle(){
        mockServer.expect(requestTo(
                "https://api.themoviedb.org/3/search/movie?query=The%20Matrix"
        ))
                .andExpect(queryParam("query", "The%20Matrix"))
                .andRespond(withStatus(HttpStatus.BAD_GATEWAY));

        Assertions.assertThrows(IntegrationException.class, () -> {
           tmdbService.searchMovieByTitle("The Matrix");
        });

        mockServer.verify();
    }

}
