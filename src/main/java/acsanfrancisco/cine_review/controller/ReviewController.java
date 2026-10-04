package acsanfrancisco.cine_review.controller;

import acsanfrancisco.cine_review.doc.ReviewControllerDoc;
import acsanfrancisco.cine_review.dto.request.CreateReviewRequest;
import acsanfrancisco.cine_review.dto.response.ReviewResponse;
import acsanfrancisco.cine_review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController implements ReviewControllerDoc {

    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReviewResponse> createReview (@RequestBody @Valid CreateReviewRequest createReviewRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reviewService.createReview(createReviewRequest));
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ReviewResponse>> findReviewsByUserId(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reviewService.findReviewsByUserId(id));
    }

    @GetMapping(value = "/users", params = "email")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ReviewResponse>> findReviewsByUserEmail(@RequestParam("email") String  email) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reviewService.findReviewsByUserEmail(email));
    }

    @GetMapping("/movies/{movieExternalId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ReviewResponse>> findReviewsByMovieExternalId(@PathVariable Long movieExternalId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reviewService.findReviewsByMovieExternalId(movieExternalId));
    }

}
