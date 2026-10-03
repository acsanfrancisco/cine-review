package acsanfrancisco.cine_review.service;

import acsanfrancisco.cine_review.dto.response.MovieResponse;
import acsanfrancisco.cine_review.entity.Movie;
import acsanfrancisco.cine_review.exception.MovieException;
import acsanfrancisco.cine_review.exception.ResourceNotFoundException;
import acsanfrancisco.cine_review.integration.MovieSearch;
import acsanfrancisco.cine_review.integration.tmdb.dto.TmdbMovieResponse;
import acsanfrancisco.cine_review.integration.tmdb.dto.TmdbSearchResponse;
import acsanfrancisco.cine_review.mapper.MovieMapper;
import acsanfrancisco.cine_review.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TmdbMovieService {

    private final MovieRepository movieRepository;
    private final MovieSearch<TmdbSearchResponse, TmdbMovieResponse> movieSearch;

    @Transactional(readOnly = true)
    public List<MovieResponse> findMoviesByTitle(String title){
        TmdbSearchResponse tmdbSearchResponse = movieSearch.searchMovieByTitle(title);
        return MovieMapper.toMovieResponse(tmdbSearchResponse);
    }

    public MovieResponse saveMovie(Long externalId){
        TmdbMovieResponse tmdbMovieResponse = movieSearch.searchMovieByExternalId(externalId);

        if(tmdbMovieResponse == null){
            throw new ResourceNotFoundException("Movie not found for external ID: " + externalId);
        }

        if(movieRepository.existsByExternalId(externalId)){
            throw new MovieException("Movie already exists for external ID: " + externalId);
        }

        MovieResponse movieResponse = MovieMapper.toMovieResponse(tmdbMovieResponse);
        Movie movie = movieRepository.save(MovieMapper.toEntity(movieResponse));
        return MovieMapper.toMovieResponse(movie);
    }
}
