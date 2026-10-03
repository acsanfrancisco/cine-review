package acsanfrancisco.cine_review.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ReviewResponse {

    private Long id;

    private Double rating;

    private String overview;

    private LocalDate createdAt;

    private Long userId;

    private String movieTitle;
}
