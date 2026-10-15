package acsanfrancisco.cine_review.service;

import acsanfrancisco.cine_review.dto.request.CreateReviewRequest;
import acsanfrancisco.cine_review.dto.response.ReviewResponse;
import acsanfrancisco.cine_review.entity.Movie;
import acsanfrancisco.cine_review.entity.Review;
import acsanfrancisco.cine_review.entity.User;
import acsanfrancisco.cine_review.exception.ResourceNotFoundException;
import acsanfrancisco.cine_review.mapper.ReviewMapper;
import acsanfrancisco.cine_review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieService movieService;
    private final UserService userService;

    @Transactional
    public ReviewResponse createReview(CreateReviewRequest createReviewRequest) {
        Review review = ReviewMapper.toEntity(createReviewRequest);
        User user = userService.findUserById(createReviewRequest.getUserId());
        Movie movie = movieService.findMovieByExternalId(createReviewRequest.getMovieExternalId());
        if(user == null) {
            throw new ResourceNotFoundException("User not found. ID = " + createReviewRequest.getUserId());
        }
        if(movie == null) {
            throw new ResourceNotFoundException("Movie not found. ID = " + createReviewRequest.getMovieExternalId());
        }
        review.setUser(user);
        review.setMovie(movie);
        return ReviewMapper.toResponse(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findReviewsByUserId(Long userId) {
        userService.findUserById(userId);
        List<Review> reviews = reviewRepository.findReviewsByUserId(userId);
        return reviews.stream().map(ReviewMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findReviewsByUserEmail(String email) {
        userService.findUserByEmail(email);
        List<Review> reviews = reviewRepository.findReviewsByUserEmail(email);
        return reviews.stream().map(ReviewMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findReviewsByMovieExternalId(Long movieExternalId) {
        movieService.findMovieByExternalId(movieExternalId);
        List<Review> reviews = reviewRepository.findReviewsByMovieExternalId(movieExternalId);
        return reviews.stream().map(ReviewMapper::toResponse).toList();
    }

}
