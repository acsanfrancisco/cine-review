package acsanfrancisco.cine_review.service;

import acsanfrancisco.cine_review.dto.request.CreateReviewRequest;
import acsanfrancisco.cine_review.dto.response.ReviewResponse;
import acsanfrancisco.cine_review.entity.Movie;
import acsanfrancisco.cine_review.entity.Review;
import acsanfrancisco.cine_review.entity.User;
import acsanfrancisco.cine_review.exception.ResourceNotFoundException;
import acsanfrancisco.cine_review.repository.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    MovieService movieService;

    @Mock
    UserService userService;

    @InjectMocks
    ReviewService reviewService;

    User user;

    Movie movie;

    Review review;

    CreateReviewRequest createReviewRequest;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setId(1L);
        user.setFullName("Little Zé da Silva");
        user.setEmail("zezinho@gmail.com");

        movie = new Movie();
        movie.setId(1L);
        movie.setExternalId(100L);
        movie.setTitle("The Matrix");
        movie.setReleaseDate(LocalDate.of(1999,3,31));
        movie.setOriginalLanguage("en");
        movie.setOriginalTitle("The Matrix");

        review = new Review();
        review.setId(1L);
        review.setRating(7.8);
        review.setCreatedAt(LocalDate.now());
        review.setUser(user);
        review.setMovie(movie);
        review.setOverview("Good movie");

        createReviewRequest = new CreateReviewRequest();
        createReviewRequest.setUserId(user.getId());
        createReviewRequest.setRating(review.getRating());
        createReviewRequest.setMovieExternalId(movie.getExternalId());
        createReviewRequest.setOverview(review.getOverview());

    }

    @Test
    @DisplayName("Should create Review when Review Request is valid")
    void shouldCreateReview() {
        Mockito.when(userService.findUserById(Mockito.any(Long.class))).thenReturn(user);
        Mockito.when(movieService.findMovieByExternalId(movie.getExternalId())).thenReturn(movie);
        Mockito.when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);

        ReviewResponse savedReview = reviewService.createReview(createReviewRequest);

        Assertions.assertEquals(createReviewRequest.getRating(), savedReview.getRating());
        Assertions.assertEquals(createReviewRequest.getOverview(), savedReview.getOverview());
        Assertions.assertEquals(review.getId(), savedReview.getId());
        Assertions.assertEquals(review.getCreatedAt(), savedReview.getCreatedAt());
        Assertions.assertEquals(user.getId(), savedReview.getUserId());
        Assertions.assertEquals(movie.getTitle(), savedReview.getMovieTitle());
        Assertions.assertEquals(movie.getExternalId(), savedReview.getMovieExternalId());
        Assertions.assertEquals(movie.getId(), savedReview.getMovieId());
        Mockito.verify(reviewRepository, Mockito.times(1)).save(Mockito.any(Review.class));
    }

    @Test
    @DisplayName("Should throw Resource Not Found Exception")
    void shouldNotSaveReviewWhenUserIdIsNotFound() {
        Mockito.when(userService.findUserById(Mockito.any(Long.class))).thenThrow(new ResourceNotFoundException("User not found for ID"));
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            reviewService.createReview(createReviewRequest);
        });
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any(Review.class));

    }

    @Test
    @DisplayName("Should throw Resource Not Found Exception")
    void shouldNotSaveReviewWhenMovieIdIsNotFound() {
        Mockito.when(movieService.findMovieByExternalId(movie.getExternalId())).thenThrow(new  ResourceNotFoundException("Movie not found for ID"));
        Mockito.when(userService.findUserById(Mockito.any(Long.class))).thenReturn(user);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            reviewService.createReview(createReviewRequest);
        });
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any(Review.class));
    }

    @Test
    @DisplayName("Should return Reviews by User ID")
    void shouldFindReviewsByUserId() {
        Mockito.when(userService.findUserById(Mockito.any(Long.class))).thenReturn(user);
        Mockito.when(reviewRepository.findReviewsByUserId(Mockito.any(Long.class))).thenReturn(List.of(review));

        List<ReviewResponse> reviewList = reviewService.findReviewsByUserId(user.getId());

        Assertions.assertFalse(reviewList.isEmpty());
        Assertions.assertEquals(1, reviewList.size());

        Mockito.verify(reviewRepository, Mockito.times(1)).findReviewsByUserId(Mockito.any(Long.class));


        Assertions.assertEquals(review.getId(), reviewList.getFirst().getId());
        Assertions.assertEquals(review.getRating(), reviewList.getFirst().getRating());
        Assertions.assertEquals(review.getOverview(), reviewList.getFirst().getOverview());
        Assertions.assertEquals(review.getCreatedAt(), reviewList.getFirst().getCreatedAt());
        Assertions.assertEquals(review.getUser().getId(), reviewList.getFirst().getUserId());
        Assertions.assertEquals(review.getMovie().getExternalId(), reviewList.getFirst().getMovieExternalId());
        Assertions.assertEquals(review.getMovie().getTitle(), reviewList.getFirst().getMovieTitle());
    }

    @Test
    @DisplayName("Should return an empty List when User has no Reviews by User ID")
    void shouldReturnAnEmptyListWhenUserHasNoReviewsByUserId() {
        Mockito.when(userService.findUserById(Mockito.any(Long.class))).thenReturn(user);
        Mockito.when(reviewRepository.findReviewsByUserId(Mockito.any(Long.class))).thenReturn(List.of());
        List<ReviewResponse> reviewList = reviewService.findReviewsByUserId(user.getId());

        Assertions.assertTrue(reviewList.isEmpty());
        Mockito.verify(reviewRepository, Mockito.times(1)).findReviewsByUserId(Mockito.any(Long.class));
    }

    @Test
    @DisplayName("Should return Reviews by User email")
    void shouldFindReviewsByUserEmail() {
        Mockito.when(userService.findUserByEmail(Mockito.any(String.class))).thenReturn(user);
        Mockito.when(reviewRepository.findReviewsByUserEmail(user.getEmail())).thenReturn(List.of(review));

        List<ReviewResponse> reviewList = reviewService.findReviewsByUserEmail(user.getEmail());

        Assertions.assertFalse(reviewList.isEmpty());
        Assertions.assertEquals(1, reviewList.size());

        Mockito.verify(reviewRepository, Mockito.times(1)).findReviewsByUserEmail(user.getEmail());

        Assertions.assertEquals(review.getId(), reviewList.getFirst().getId());
        Assertions.assertEquals(review.getRating(), reviewList.getFirst().getRating());
        Assertions.assertEquals(review.getOverview(), reviewList.getFirst().getOverview());
        Assertions.assertEquals(review.getCreatedAt(), reviewList.getFirst().getCreatedAt());
        Assertions.assertEquals(review.getUser().getId(), reviewList.getFirst().getUserId());
        Assertions.assertEquals(review.getMovie().getExternalId(), reviewList.getFirst().getMovieExternalId());
        Assertions.assertEquals(review.getMovie().getTitle(), reviewList.getFirst().getMovieTitle());
    }

    @Test
    @DisplayName("Should return an empty List when User has no Reviews by User email")
    void shouldReturnAnEmptyListWhenUserHasNoReviewsByUserEmail() {
        Mockito.when(userService.findUserByEmail(Mockito.any(String.class))).thenReturn(user);
        Mockito.when(reviewRepository.findReviewsByUserEmail(Mockito.any(String.class))).thenReturn(List.of());
        List<ReviewResponse> reviewList = reviewService.findReviewsByUserEmail(user.getEmail());

        Assertions.assertTrue(reviewList.isEmpty());
        Mockito.verify(reviewRepository, Mockito.times(1)).findReviewsByUserEmail(Mockito.any(String.class));
    }

    @Test
    @DisplayName("Should return Reviews by Movie external ID")
    void shouldReturnReviewsByMovieExternalId() {
        Mockito.when(movieService.findMovieByExternalId(Mockito.any(Long.class))).thenReturn(movie);
        Mockito.when(reviewRepository.findReviewsByMovieExternalId(movie.getExternalId())).thenReturn(List.of(review));

        List<ReviewResponse> reviewList = reviewService.findReviewsByMovieExternalId(movie.getExternalId());

        Assertions.assertFalse(reviewList.isEmpty());
        Assertions.assertEquals(1, reviewList.size());
        Mockito.verify(reviewRepository, Mockito.times(1)).findReviewsByMovieExternalId(movie.getExternalId());

        Assertions.assertEquals(review.getId(), reviewList.getFirst().getId());
        Assertions.assertEquals(review.getRating(), reviewList.getFirst().getRating());
        Assertions.assertEquals(review.getOverview(), reviewList.getFirst().getOverview());
        Assertions.assertEquals(review.getCreatedAt(), reviewList.getFirst().getCreatedAt());
        Assertions.assertEquals(review.getUser().getId(), reviewList.getFirst().getUserId());
        Assertions.assertEquals(review.getMovie().getExternalId(), reviewList.getFirst().getMovieExternalId());
        Assertions.assertEquals(review.getMovie().getTitle(), reviewList.getFirst().getMovieTitle());
    }

    @Test
    @DisplayName("Should return an empty List when Movie has no Reviews by external ID")
    void shouldReturnAnEmptyListWhenMovieHasNoReviewsByExternalId() {
        Mockito.when(movieService.findMovieByExternalId(Mockito.any(Long.class))).thenReturn(movie);
        Mockito.when(reviewRepository.findReviewsByMovieExternalId(Mockito.any(Long.class))).thenReturn(List.of());

        List<ReviewResponse> reviewList = reviewService.findReviewsByMovieExternalId(movie.getExternalId());

        Assertions.assertTrue(reviewList.isEmpty());
        Mockito.verify(reviewRepository, Mockito.times(1)).findReviewsByMovieExternalId(Mockito.any(Long.class));
    }

    @Test
    void shouldNotFindReviewsByUserIdWhenUserIsNotFound() {
        Mockito.when(userService.findUserById(Mockito.any(Long.class))).thenThrow(new ResourceNotFoundException("User not found for ID"));

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            reviewService.findReviewsByUserId(user.getId());
        });

        Mockito.verify(reviewRepository, Mockito.never()).findReviewsByUserId(Mockito.any(Long.class));
    }

    @Test
    void shouldNotFindReviewsByUserEmailWhenUserIsNotFound() {
        Mockito.when(userService.findUserByEmail(Mockito.any(String.class))).thenThrow(new ResourceNotFoundException("User not found for email"));
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            reviewService.findReviewsByUserEmail(user.getEmail());
        });

        Mockito.verify(reviewRepository, Mockito.never()).findReviewsByUserEmail(Mockito.any(String.class));
    }

    @Test
    void shouldNotFindReviewsByMovieExternalIdWhenMovieIsNotFound() {
        Mockito.when(movieService
                .findMovieByExternalId(Mockito.any(Long.class))).thenThrow(new ResourceNotFoundException("Movie not found for external ID"));

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            reviewService.findReviewsByMovieExternalId(movie.getExternalId());
        });

        Mockito.verify(reviewRepository, Mockito.never()).findReviewsByMovieExternalId(Mockito.any(Long.class));
    }
}
