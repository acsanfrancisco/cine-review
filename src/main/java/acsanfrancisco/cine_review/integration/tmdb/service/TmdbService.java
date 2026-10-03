package acsanfrancisco.cine_review.integration.tmdb.service;

import acsanfrancisco.cine_review.exception.IntegrationException;
import acsanfrancisco.cine_review.integration.MovieSearch;
import acsanfrancisco.cine_review.integration.tmdb.dto.TmdbMovieResponse;
import acsanfrancisco.cine_review.integration.tmdb.dto.TmdbSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
public class TmdbService implements MovieSearch<TmdbSearchResponse,  TmdbMovieResponse> {

    private final RestClient restClient;

    @Override
    public TmdbSearchResponse searchMovieByTitle(String title) {
        try{
            return restClient
                    .get()
                    .uri(uriBuilder ->
                            uriBuilder.path("/search/movie")
                                    .queryParam("query", title)
                                    .build())
                    .retrieve()
                    .body(TmdbSearchResponse.class);

        } catch(RestClientException e) {
            throw new IntegrationException("Could not reach TMDB API. Error Message = " + e.getMessage());
        }
    }

    @Override
    public TmdbMovieResponse searchMovieByExternalId(Long externalId) {
        try{
            return restClient
                    .get()
                    .uri("/movie/{id}", externalId)
                    .retrieve()
                    .body(TmdbMovieResponse.class);

        } catch(RestClientException e) {
            throw new IntegrationException("Could not reach TMDB API. Error Message = " + e.getMessage());
        }
    }

}
