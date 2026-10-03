package acsanfrancisco.cine_review.integration.tmdb.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Configuration
public class TmdbRestClient {

    private final TmdbConfiguration tmdbConfiguration;

    @Bean
    public RestClient restClient(){
        return RestClient.builder()
                .baseUrl(tmdbConfiguration.getTmdbUrl())
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + tmdbConfiguration.getTmdbToken())
                .defaultHeader(
                        HttpHeaders.ACCEPT,
                        MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
