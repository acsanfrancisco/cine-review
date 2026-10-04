package acsanfrancisco.cine_review.controller;

import acsanfrancisco.cine_review.doc.MovieControllerDoc;
import acsanfrancisco.cine_review.dto.response.MovieResponse;
import acsanfrancisco.cine_review.mapper.MovieMapper;
import acsanfrancisco.cine_review.service.MovieService;
import acsanfrancisco.cine_review.service.TmdbMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController implements MovieControllerDoc {

    private final TmdbMovieService tmdbMovieService;
    private final MovieService movieService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<MovieResponse>> findMoviesByTitle(@RequestParam String title) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tmdbMovieService.findMoviesByTitle(title));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MovieResponse> saveMovie(@RequestParam Long externalId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tmdbMovieService.saveMovie(externalId));
    }

    @GetMapping("/external/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MovieResponse> findSavedMoviesByExternalId(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(MovieMapper.toMovieResponse(movieService.findSavedMovieByExternalId(id)));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MovieResponse> findMovieById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(MovieMapper.toMovieResponse(movieService.findMovieById(id)));
    }

}
