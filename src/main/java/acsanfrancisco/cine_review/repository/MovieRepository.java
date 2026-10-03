package acsanfrancisco.cine_review.repository;

import acsanfrancisco.cine_review.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    boolean existsByExternalId(Long externalId);

    Optional<Movie> findByExternalId(Long externalId);
}
