package acsanfrancisco.cine_review.doc;

import acsanfrancisco.cine_review.dto.response.MovieResponse;
import acsanfrancisco.cine_review.exception.handler.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Tag(
        name = "Movies",
        description = "Endpoints for searching movies through TMDB, saving movies to the application," +
                " and retrieving saved movies by external or internal ID."
)
public interface MovieControllerDoc {

    @Operation(
            summary = "Search movies by title",
            description = "Searches for movies by title using the TMDB API and returns the matching movies",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Movies successfully found"
                    ),
                    @ApiResponse(
                            responseCode = "502",
                            description = "TMDB service not available",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<List<MovieResponse>> findMoviesByTitle(@RequestParam
                                                          @Parameter(description = "Movie title", required = true, example = "The Batman")
                                                          String title);

    @Operation(
            summary = "Save movie by external ID",
            description = "Save movie in the internal Database through external movie ID",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Movie saved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Movie not found for external ID",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Movie already exists for external ID",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "502",
                            description = "TMDB service not available",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<MovieResponse> saveMovie(@RequestParam
                                            @Parameter(description = "Movie external ID", required = true, example = "500")
                                            Long externalId);

    @Operation(
            summary = "Find movie by external ID",
            description = "Find a movie saved in the database by the external ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Movie found successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Movie not found for external ID in the database",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<MovieResponse> findSavedMoviesByExternalId(@PathVariable
                                                              @Parameter(description = "Movie external ID", required = true, example = "500")
                                                              Long externalId);

    @Operation(
            summary = "Find movie by ID",
            description = "Find a movie in the database by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Movie found successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Movie not found for ID",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<MovieResponse> findMovieById(@PathVariable
                                                @Parameter(description = "Movie ID", required = true, example = "1")
                                                Long id);
}
