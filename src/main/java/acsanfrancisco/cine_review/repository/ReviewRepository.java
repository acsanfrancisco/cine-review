package acsanfrancisco.cine_review.repository;

import acsanfrancisco.cine_review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
    SELECT r
    FROM Review r
    WHERE r.user.id = :userId
""")
    List<Review> findReviewsByUserId(@Param("userId") Long userId);

    @Query("""
    SELECT r
    FROM Review r
    WHERE r.user.email = :email
""")
    List<Review> findReviewsByUserEmail(@Param("email") String email);

    @Query("""
    SELECT r
    FROM Review r
    WHERE r.movie.externalId = :externalId
""")
    List<Review> findReviewsByMovieExternalId(@Param("externalId")Long externalId);
}
