package acsanfrancisco.cine_review.doc;

import acsanfrancisco.cine_review.dto.request.CreateReviewRequest;
import acsanfrancisco.cine_review.dto.response.ReviewResponse;
import acsanfrancisco.cine_review.exception.handler.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Tag(
        name = "Reviews",
        description = "Endpoints responsible for review management, including creating, fetching by user ID," +
                " fetching by user email, and fetching by movie external id"
)
public interface ReviewControllerDoc {

    @Operation(
            summary = "Create review",
            description = "Creates a new review related to a movie",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created review successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found for ID",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Movie not found for external ID",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "502",
                            description = "TMDB service not available",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<ReviewResponse> createReview(@RequestBody @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Requested data to create an review",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateReviewRequest.class),
                    examples = @ExampleObject(
                            name = "Valid review schema",
                            value = """
                                    {
                                        "userId" : 1,
                                        "movieExternalId" : 500,
                                        "rating": 10.0,
                                        "overview": "Very good movie, but I would not watch it again"
                                    }
                                    """
                    ))
            ) CreateReviewRequest createReviewRequest);

    @Operation(
            summary = "Find reviews by user ID",
            description = "Find reviews related to a user by his ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reviews found successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found for ID",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<List<ReviewResponse>> findReviewsByUserId(@PathVariable
                                                             @Parameter(description = "User ID", required = true, example = "1")
                                                             Long id);

    @Operation(
            summary = "Find reviews by user email",
            description = "Find reviews related to a user by his email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reviews found successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found for ID",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<List<ReviewResponse>> findReviewsByUserEmail(@RequestParam
                                                                @Parameter(description = "User email", required = true, example = "littlezedasilva@gmail.com")
                                                                String email);

    @Operation(
            summary = "Find reviews by movie external ID",
            description = "Find reviews related to a movie by the external ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reviews found successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Movie not found for external ID",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<List<ReviewResponse>> findReviewsByMovieExternalId(@PathVariable
                                                                      @Parameter(description = "Movie external ID", required = true, example = "500")
                                                                      Long movieExternalId);
}
