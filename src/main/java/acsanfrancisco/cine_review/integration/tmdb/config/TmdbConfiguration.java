package acsanfrancisco.cine_review.integration.tmdb.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TmdbConfiguration {

    @Value("${TMDB_API_URL}")
    private String tmdbUrl;

    @Value("${TMDB_API_TOKEN}")
    private String tmdbToken;
}
