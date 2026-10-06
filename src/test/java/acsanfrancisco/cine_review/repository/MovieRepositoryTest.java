package acsanfrancisco.cine_review.repository;

import acsanfrancisco.cine_review.entity.Movie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void shouldSaveMovie(){
        Movie movie = new Movie();
        movie.setExternalId(100L);
        movie.setTitle("Batman Returns");
        movie.setOriginalLanguage("en");
        movie.setReleaseDate(LocalDate.of(1992, 6, 19));

        movieRepository.save(movie);
        Assertions.assertNotNull(movie.getId());
        Optional<Movie> foundMovie = movieRepository.findById(movie.getId());
        Assertions.assertTrue(foundMovie.isPresent());
        Assertions.assertEquals(movie.getId(), foundMovie.get().getId());
        Assertions.assertEquals(movie.getExternalId(), foundMovie.get().getExternalId());
        Assertions.assertEquals(movie.getTitle(), foundMovie.get().getTitle());
        Assertions.assertEquals(movie.getOriginalLanguage(), foundMovie.get().getOriginalLanguage());
        Assertions.assertEquals(movie.getReleaseDate(), foundMovie.get().getReleaseDate());
    }

    @Test
    void shouldNotSaveMovieWithNullTitle(){
        Movie movie = new Movie();
        movie.setExternalId(100L);
        movie.setTitle(null);
        movie.setOriginalLanguage("en");
        movie.setReleaseDate(LocalDate.of(1992, 6, 19));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            movieRepository.saveAndFlush(movie);
        });
    }

    @Test
    void shouldNotSaveMovieWithDuplicatedExternalId(){
        Movie movie = new Movie();
        movie.setExternalId(100L);
        movie.setTitle("Batman Returns");
        movie.setOriginalLanguage("en");
        movie.setReleaseDate(LocalDate.of(1992, 6, 19));
        movieRepository.saveAndFlush(movie);

        Movie duplicatedExternalId = new Movie();
        duplicatedExternalId.setExternalId(100L);
        duplicatedExternalId.setTitle("Batman Does Not Return");
        duplicatedExternalId.setOriginalLanguage("en");
        duplicatedExternalId.setReleaseDate(LocalDate.of(1992, 6, 19));

        Assertions.assertThrows(DataIntegrityViolationException.class, () ->{
            movieRepository.saveAndFlush(duplicatedExternalId);
        });
    }

    @Test
    void shouldDeleteMovie(){
        Movie movie = new Movie();
        movie.setExternalId(100L);
        movie.setTitle("Batman Returns");
        movie.setOriginalLanguage("en");
        movie.setReleaseDate(LocalDate.of(1992, 6, 19));
        movieRepository.save(movie);

        Assertions.assertNotNull(movie.getId());
        movieRepository.delete(movie);
        Assertions.assertFalse(movieRepository.findById(movie.getId()).isPresent());
    }

    @Test
    void shouldFindMovieById(){
        Movie movie = new Movie();
        movie.setExternalId(100L);
        movie.setTitle("Batman Returns");
        movie.setOriginalLanguage("en");
        movie.setReleaseDate(LocalDate.of(1992, 6, 19));

        movieRepository.save(movie);

        Optional<Movie> foundMovie = movieRepository.findById(movie.getId());
        Assertions.assertTrue(foundMovie.isPresent());
        Assertions.assertEquals(movie.getId(), foundMovie.get().getId());
        Assertions.assertEquals(movie.getExternalId(), foundMovie.get().getExternalId());
        Assertions.assertEquals(movie.getTitle(), foundMovie.get().getTitle());
        Assertions.assertEquals(movie.getOriginalLanguage(), foundMovie.get().getOriginalLanguage());
        Assertions.assertEquals(movie.getReleaseDate(), foundMovie.get().getReleaseDate());
    }

    @Test
    @Sql("/sql/seeding-movies.sql")
    void shouldNotFindMovieById(){
        Optional<Movie> foundMovie = movieRepository.findById(4L);
        Assertions.assertFalse(foundMovie.isPresent());
    }

    @Test
    @Sql("/sql/seeding-movies.sql")
    void shouldFindMovieByExternalId(){
        Optional<Movie> foundMovie = movieRepository.findByExternalId(100L);
        Assertions.assertTrue(foundMovie.isPresent());
        Assertions.assertEquals(1L, foundMovie.get().getId());
        Assertions.assertEquals(100L, foundMovie.get().getExternalId());
        Assertions.assertEquals("The Matrix", foundMovie.get().getTitle());
        Assertions.assertEquals(LocalDate.of(1999,3,31), foundMovie.get().getReleaseDate());
        Assertions.assertEquals("en", foundMovie.get().getOriginalLanguage());
        Assertions.assertEquals("The Matrix", foundMovie.get().getOriginalTitle());
    }

    @Test
    @Sql("/sql/seeding-movies.sql")
    void shouldNotFindMovieByExternalId(){
        Optional<Movie> foundMovie = movieRepository.findByExternalId(500L);
        Assertions.assertFalse(foundMovie.isPresent());
    }

    @Test
    @Sql("/sql/seeding-movies.sql")
    void shouldReturnTrueToExistsByExternalId(){
        boolean exists = movieRepository.existsByExternalId(100L);
        Assertions.assertTrue(exists);
    }

    @Test
    @Sql("/sql/seeding-movies.sql")
    void shouldReturnFalseToExistsByExternalId(){
        boolean exists = movieRepository.existsByExternalId(500L);
        Assertions.assertFalse(exists);
    }
}
