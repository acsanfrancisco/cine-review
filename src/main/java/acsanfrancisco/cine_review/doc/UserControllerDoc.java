package acsanfrancisco.cine_review.doc;

import acsanfrancisco.cine_review.dto.request.CreateUserRequest;
import acsanfrancisco.cine_review.dto.response.UserResponse;
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

@Tag(
        name = "Users",
        description = "Endpoints responsible for user management, including creating, fetching by ID" +
                ", fetching by email, and deletion. "
)
public interface UserControllerDoc {

    @Operation(
            summary = "Create user",
            description = "Creates a new user in the Cine Review system",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created user successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation Error",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<UserResponse> createUser(@RequestBody @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Requested data to create a user",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateUserRequest.class),
                    examples = @ExampleObject (
                            name = "Valid user schema",
                            value = """
                                    {
                                        "fullName" : "Little Zé da Silva",
                                        "email" : "littlezedasilva@gmail.com"
                                    }
                                    """
                    ))
            ) CreateUserRequest createUserRequest);

    @Operation(
            summary = "Delete user",
            description = "Delete a existing user from the Cine Review system",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Deleted user successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found for ID",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Must not delete a user with Reviews",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<Void> deleteUser(@PathVariable
                    @Parameter(description = "User ID", required = true, example = "1")
                    Long id);

    @Operation(
            summary = "Find an user by ID",
            description = "Search a specific user by his ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User successfully found"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found for ID",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<UserResponse> findUserById(@PathVariable
                              @Parameter(description = "User ID", required = true, example = "1")
                              Long id);

    @Operation(
            summary = "Find a user by email",
            description = "Search a specific user by his email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User successfully found"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found for email",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<UserResponse> findUserByEmail(@RequestParam
                                 @Parameter(description = "User email", required = true, example = "littlezedasilva@gmail.com")
                                 String email);
}
