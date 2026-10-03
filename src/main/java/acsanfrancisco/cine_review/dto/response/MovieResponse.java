package acsanfrancisco.cine_review.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class MovieResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    private Long externalId;

    private String title;

    private LocalDate releaseDate;

    private String originalLanguage;

    private String originalTitle;
}
