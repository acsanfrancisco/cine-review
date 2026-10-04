package acsanfrancisco.cine_review.controller;

import acsanfrancisco.cine_review.doc.UserControllerDoc;
import acsanfrancisco.cine_review.dto.request.CreateUserRequest;
import acsanfrancisco.cine_review.dto.response.UserResponse;
import acsanfrancisco.cine_review.mapper.UserMapper;
import acsanfrancisco.cine_review.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDoc {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(createUserRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity
                .noContent().build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserMapper.toResponse(userService.findUserById(id)));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> findUserByEmail(@RequestParam String email){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserMapper.toResponse(userService.findUserByEmail(email)));
    }
}
