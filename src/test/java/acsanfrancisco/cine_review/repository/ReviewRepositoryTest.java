package acsanfrancisco.cine_review.repository;

import acsanfrancisco.cine_review.entity.Movie;
import acsanfrancisco.cine_review.entity.Review;
import acsanfrancisco.cine_review.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldSaveReview() {
        User user = new User();
        user.setFullName("Little Zé da Silva");
        user.setEmail("zezinho@gmail.com");
        entityManager.persist(user);

        Movie movie = new Movie();
        movie.setExternalId(100L);
        movie.setTitle("Batman Returns");
        movie.setOriginalLanguage("en");
        movie.setReleaseDate(LocalDate.of(1992, 6, 19));
        entityManager.persist(movie);

        Review review = new Review();
        review.setCreatedAt(LocalDate.now());
        review.setOverview("One the best super hero movies ever made");
        review.setRating(9.5);
        review.setUser(user);
        review.setMovie(movie);
        reviewRepository.save(review);

        Assertions.assertNotNull(review.getId());
        Optional<Review> foundReview = reviewRepository.findById(review.getId());
        Assertions.assertTrue(foundReview.isPresent());
        Assertions.assertEquals(review.getCreatedAt(), foundReview.get().getCreatedAt());
        Assertions.assertEquals(review.getOverview(), foundReview.get().getOverview());
        Assertions.assertEquals(review.getRating(), foundReview.get().getRating());
        Assertions.assertEquals(review.getUser(), foundReview.get().getUser());
        Assertions.assertEquals(review.getMovie(), foundReview.get().getMovie());
    }

    @Test
    void shouldNotSaveReviewWhenUserIsNull(){
        Movie movie = new Movie();
        movie.setExternalId(100L);
        movie.setTitle("Batman Returns");
        movie.setOriginalLanguage("en");
        movie.setReleaseDate(LocalDate.of(1992, 6, 19));
        entityManager.persist(movie);

        Review review = new Review();
        review.setCreatedAt(LocalDate.now());
        review.setOverview("One the best super hero movies ever made");
        review.setRating(9.5);
        review.setUser(null);
        review.setMovie(movie);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            reviewRepository.saveAndFlush(review);
        });
    }

    @Test
    void shouldNotSaveReviewWhenMovieIsNull(){
        User user = new User();
        user.setFullName("Little Zé da Silva");
        user.setEmail("zezinho@gmail.com");
        entityManager.persist(user);

        Review review = new Review();
        review.setCreatedAt(LocalDate.now());
        review.setOverview("One the best super hero movies ever made");
        review.setRating(9.5);
        review.setUser(user);
        review.setMovie(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            reviewRepository.saveAndFlush(review);
        });
    }

    @Test
    void shouldDeleteReview(){
        User user = new User();
        user.setFullName("Little Zé da Silva");
        user.setEmail("zezinho@gmail.com");
        entityManager.persist(user);

        Movie movie = new Movie();
        movie.setExternalId(100L);
        movie.setTitle("Batman Returns");
        movie.setOriginalLanguage("en");
        movie.setReleaseDate(LocalDate.of(1992, 6, 19));
        entityManager.persist(movie);

        Review review = new Review();
        review.setCreatedAt(LocalDate.now());
        review.setOverview("One the best super hero movies ever made");
        review.setRating(9.5);
        review.setUser(user);
        review.setMovie(movie);
        reviewRepository.save(review);

        Assertions.assertNotNull(review.getId());
        reviewRepository.delete(review);
        Assertions.assertFalse(reviewRepository.findById(review.getId()).isPresent());

    }

    @Test
    void shouldFindReviewById(){
        User user = new User();
        user.setFullName("Little Zé da Silva");
        user.setEmail("zezinho@gmail.com");
        entityManager.persist(user);

        Movie movie = new Movie();
        movie.setExternalId(100L);
        movie.setTitle("Batman Returns");
        movie.setOriginalLanguage("en");
        movie.setReleaseDate(LocalDate.of(1992, 6, 19));
        entityManager.persist(movie);

        Review review = new Review();
        review.setCreatedAt(LocalDate.now());
        review.setOverview("One the best super hero movies ever made");
        review.setRating(9.5);
        review.setUser(user);
        review.setMovie(movie);
        reviewRepository.save(review);

        Optional<Review> foundReview = reviewRepository.findById(review.getId());
        Assertions.assertTrue(foundReview.isPresent());
        Assertions.assertEquals(review.getId(), foundReview.get().getId());
        Assertions.assertEquals(review.getCreatedAt(), foundReview.get().getCreatedAt());
        Assertions.assertEquals(review.getOverview(), foundReview.get().getOverview());
        Assertions.assertEquals(review.getRating(), foundReview.get().getRating());
        Assertions.assertEquals(review.getUser().getId(), foundReview.get().getUser().getId());
        Assertions.assertEquals(review.getMovie().getId(), foundReview.get().getMovie().getId());
    }

    @Test
   @Sql({
           "/sql/seeding-users.sql",
           "/sql/seeding-movies.sql",
           "/sql/seeding-reviews.sql"
   })
    void shouldNotFindReviewById(){
        Optional<Review> foundReview = reviewRepository.findById(4L);
        Assertions.assertFalse(foundReview.isPresent());
    }

    @Test
    @Sql({
            "/sql/seeding-users.sql",
            "/sql/seeding-movies.sql",
            "/sql/seeding-reviews.sql"
    })
    void shouldFindReviewsByUserId(){
        List<Review> foundReview = reviewRepository.findReviewsByUserId(1L);
        Assertions.assertFalse(foundReview.isEmpty());
        Assertions.assertEquals(1, foundReview.size());
    }

    @Test
    @Sql({
            "/sql/seeding-users.sql",
            "/sql/seeding-movies.sql",
            "/sql/seeding-reviews.sql"
    })
    void shouldNotFindReviewsByUserId(){
        List<Review> foundReviews = reviewRepository.findReviewsByUserId(4L);
        Assertions.assertTrue(foundReviews.isEmpty());
    }

    @Test
    @Sql({
            "/sql/seeding-users.sql",
            "/sql/seeding-movies.sql",
            "/sql/seeding-reviews.sql"
    })
    void shouldFindReviewsByUserEmail(){
        List<Review> foundReviews = reviewRepository.findReviewsByUserEmail("zezinho@gmail.com");
        Assertions.assertFalse(foundReviews.isEmpty());
        Assertions.assertEquals(1, foundReviews.size());
    }

    @Test
    @Sql({
            "/sql/seeding-users.sql",
            "/sql/seeding-movies.sql",
            "/sql/seeding-reviews.sql"
    })
    void shouldNotFindReviewsByUserEmail(){
        List<Review> foundReviews = reviewRepository.findReviewsByUserEmail("invalido@gmail.com");
        Assertions.assertTrue(foundReviews.isEmpty());
    }

    @Test
    @Sql({
            "/sql/seeding-users.sql",
            "/sql/seeding-movies.sql",
            "/sql/seeding-reviews.sql"
    })
    void shouldFindReviewsByMovieExternalId(){
        List<Review> foundReviews = reviewRepository.findReviewsByMovieExternalId(100L);
        Assertions.assertFalse(foundReviews.isEmpty());
        Assertions.assertEquals(1, foundReviews.size());
    }

    @Test
    @Sql({
            "/sql/seeding-users.sql",
            "/sql/seeding-movies.sql",
            "/sql/seeding-reviews.sql"
    })
    void shouldNotFindReviewsByMovieExternalId(){
        List<Review> foundReviews = reviewRepository.findReviewsByMovieExternalId(500L);
        Assertions.assertTrue(foundReviews.isEmpty());
    }
}
