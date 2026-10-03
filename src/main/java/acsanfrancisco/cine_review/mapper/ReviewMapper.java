package acsanfrancisco.cine_review.mapper;

import acsanfrancisco.cine_review.dto.request.CreateReviewRequest;
import acsanfrancisco.cine_review.dto.response.ReviewResponse;
import acsanfrancisco.cine_review.entity.Review;

import java.time.LocalDate;

public class ReviewMapper {

    public static Review toEntity(CreateReviewRequest createReviewRequest) {
        Review review = new Review();
        review.setRating(createReviewRequest.getRating());
        review.setOverview(createReviewRequest.getOverview());
        review.setCreatedAt(LocalDate.now());
        return review;
    }

    public static ReviewResponse toResponse(Review review) {
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setId(review.getId());
        reviewResponse.setRating(review.getRating());
        reviewResponse.setOverview(review.getOverview());
        reviewResponse.setCreatedAt(review.getCreatedAt());
        reviewResponse.setMovieTitle(review.getMovie().getTitle());
        reviewResponse.setUserId(review.getUser().getId());
        return reviewResponse;
    }
}
