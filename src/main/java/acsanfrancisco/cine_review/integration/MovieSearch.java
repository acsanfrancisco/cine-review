package acsanfrancisco.cine_review.integration;

public interface MovieSearch<S, M> {

    S searchMovieByTitle(String title);

    M searchMovieByExternalId(Long externalId);
}
