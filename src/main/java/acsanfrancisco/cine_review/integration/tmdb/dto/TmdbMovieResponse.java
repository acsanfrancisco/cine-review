package acsanfrancisco.cine_review.integration.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class TmdbMovieResponse {

    private Long id;

    private String title;

    @JsonProperty("release_date")
    private LocalDate releaseDate;

    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("original_title")
    private String originalTitle;
}
