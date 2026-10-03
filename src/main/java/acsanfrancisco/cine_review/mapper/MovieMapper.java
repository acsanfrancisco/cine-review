package acsanfrancisco.cine_review.mapper;

import acsanfrancisco.cine_review.dto.response.MovieResponse;
import acsanfrancisco.cine_review.entity.Movie;
import acsanfrancisco.cine_review.integration.tmdb.dto.TmdbMovieResponse;
import acsanfrancisco.cine_review.integration.tmdb.dto.TmdbSearchResponse;
import java.util.List;

public class MovieMapper {

    public static MovieResponse toMovieResponse(Movie movie){
        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setId(movie.getId());
        movieResponse.setExternalId(movie.getExternalId());
        movieResponse.setTitle(movie.getTitle());
        movieResponse.setOriginalTitle(movie.getOriginalTitle());
        movieResponse.setReleaseDate(movie.getReleaseDate());
        movieResponse.setOriginalLanguage(movie.getOriginalLanguage());
        return movieResponse;
    }

    public static MovieResponse toMovieResponse(TmdbMovieResponse tmdbMovieResponse){
        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setExternalId(tmdbMovieResponse.getId());
        movieResponse.setTitle(tmdbMovieResponse.getTitle());
        movieResponse.setOriginalTitle(tmdbMovieResponse.getOriginalTitle());
        movieResponse.setReleaseDate(tmdbMovieResponse.getReleaseDate());
        movieResponse.setOriginalLanguage(tmdbMovieResponse.getOriginalLanguage());
        return movieResponse;
    }

    public static List<MovieResponse> toMovieResponse(TmdbSearchResponse searchResponse) {
       return searchResponse.getResults().stream()
               .map(MovieMapper::toMovieResponse)
               .toList();
    }

    public static Movie toEntity(MovieResponse movieResponse){
        Movie movie = new Movie();
        movie.setExternalId(movieResponse.getExternalId());
        movie.setTitle(movieResponse.getTitle());
        movie.setOriginalTitle(movieResponse.getOriginalTitle());
        movie.setReleaseDate(movieResponse.getReleaseDate());
        movie.setOriginalLanguage(movieResponse.getOriginalLanguage());
        return movie;
    }

}
