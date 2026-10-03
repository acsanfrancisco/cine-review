package acsanfrancisco.cine_review.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewRequest {

    @NotNull(message = "Must inform user ID")
    Long userId;

    @NotNull(message = "Must inform movie external ID")
    Long movieExternalId;

    @NotNull(message = "Must inform rating")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
    private Double rating;

    @NotBlank(message = "Must inform overview")
    private String overview;
}
