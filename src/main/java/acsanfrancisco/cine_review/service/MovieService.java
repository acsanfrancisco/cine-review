package acsanfrancisco.cine_review.service;

import acsanfrancisco.cine_review.entity.Movie;
import acsanfrancisco.cine_review.exception.ResourceNotFoundException;
import acsanfrancisco.cine_review.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public Movie findSavedMovieByExternalId(Long movieExternalId) {
        return movieRepository.findByExternalId(movieExternalId)
                .orElseThrow(()-> new ResourceNotFoundException("Movie not found for External ID: " + movieExternalId));
    }

    @Transactional(readOnly = true)
    public Movie findMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Movie not found for Id: " + id));
    }
}
