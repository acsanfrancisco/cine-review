package acsanfrancisco.cine_review.controller;

import acsanfrancisco.cine_review.dto.request.CreateReviewRequest;
import acsanfrancisco.cine_review.dto.response.ReviewResponse;
import acsanfrancisco.cine_review.entity.Movie;
import acsanfrancisco.cine_review.entity.Review;
import acsanfrancisco.cine_review.entity.User;
import acsanfrancisco.cine_review.exception.ResourceNotFoundException;
import acsanfrancisco.cine_review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;
import java.util.List;

@WebMvcTest(ReviewController.class)
public class ReviewControllerTest {

    @MockitoBean
    ReviewService reviewService;

    @Autowired
    MockMvc mockMvc;

    CreateReviewRequest createReviewRequest;

    User user;

    ReviewResponse response;

    Review review;

    Movie movie;

    String json = """
            {
                "userId" : 1,
                "movieExternalId" : 100,
                "rating" : 10.0,
                "overview" : "Of course it's good. It's batman"
            }""";

    @BeforeEach
    public void setup() {
        createReviewRequest = new CreateReviewRequest();
        createReviewRequest.setUserId(1L);
        createReviewRequest.setMovieExternalId(100L);
        createReviewRequest.setOverview("Of course it's good. It's batman");
        createReviewRequest.setRating(10.0);

        response = new ReviewResponse();
        response.setId(1L);
        response.setUserId(1L);
        response.setMovieExternalId(100L);
        response.setMovieTitle("The Batman");
        response.setOverview("Of course it's good. It's batman");
        response.setCreatedAt(LocalDate.now());
        response.setRating(10.0);

        user = new User();
        user.setId(1L);
        user.setEmail("zezinho@gmail.com");
        user.setFullName("Little Zé da Silva");

        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("The Batman");
        movie.setOriginalTitle("The Batman");
        movie.setReleaseDate(LocalDate.of(1998, 2, 13));
        movie.setExternalId(100L);

        review = new Review();
        review.setId(1L);
        review.setMovie(movie);
        review.setUser(user);
        review.setRating(10.0);
        review.setOverview("Of course it's good. It's batman");
        review.setCreatedAt(LocalDate.now());
    }

    @Test
    void shouldCreateReview() throws Exception {
        Mockito.when(reviewService.createReview(Mockito.any(CreateReviewRequest.class))).thenReturn(response);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(review.getCreatedAt().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(review.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieExternalId").value(createReviewRequest.getMovieExternalId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieTitle").value(movie.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(review.getRating()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.overview").value(review.getOverview()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(user.getId()));
    }

    @Test
    void shouldNotCreateReviewWhenUserIsInvalid() throws Exception {
        Mockito.when(reviewService.createReview(Mockito.any(CreateReviewRequest.class))).thenThrow(new ResourceNotFoundException("User not found for ID"));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found for ID"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.method").value("POST"));
    }

    @Test
    void shouldNotCreateReviewWhenMovieIsInvalid() throws Exception {
        Mockito.when(reviewService.createReview(Mockito.any(CreateReviewRequest.class))).thenThrow(new ResourceNotFoundException("Movie not found for ID"));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Movie not found for ID"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.method").value("POST"));
    }

    @Test
    void shouldFindReviewByUserId() throws Exception {
        Mockito.when(reviewService.findReviewsByUserId(Mockito.any(Long.class))).thenReturn(List.of(response));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/reviews/users/" + user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].createdAt").value(review.getCreatedAt().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(review.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].movieExternalId").value(createReviewRequest.getMovieExternalId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].movieTitle").value(movie.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rating").value(review.getRating()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].overview").value(review.getOverview()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(user.getId()));
    }

    @Test
    void shouldNotFindReviewByUserIdWhenUserIsInvalid() throws Exception {
        Mockito.when(reviewService.findReviewsByUserId(Mockito.any(Long.class))).thenThrow(new ResourceNotFoundException("User not found for ID"));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/reviews/users/" + user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found for ID"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.method").value("GET"));
    }

    @Test
    void shouldFindReviewsByUserEMail() throws Exception {
        Mockito.when(reviewService.findReviewsByUserEmail(Mockito.any(String.class))).thenReturn(List.of(response));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/reviews/users")
                                .param("email", user.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].createdAt").value(review.getCreatedAt().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(review.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].movieExternalId").value(createReviewRequest.getMovieExternalId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].movieTitle").value(movie.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rating").value(review.getRating()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].overview").value(review.getOverview()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(user.getId()));
    }

    @Test
    void shouldNotFindReviewsByUserEmailWhenUserIsInvalid() throws Exception {
        Mockito.when(reviewService.findReviewsByUserEmail(Mockito.any(String.class))).thenThrow(new ResourceNotFoundException("User not found for email"));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/reviews/users")
                                .param("email", user.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found for email"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.method").value("GET"));
    }

    @Test
    void shouldFindReviewsByMovieExternalId() throws Exception {
        Mockito.when(reviewService.findReviewsByMovieExternalId(Mockito.any(Long.class))).thenReturn(List.of(response));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/reviews/movies/" + movie.getExternalId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].createdAt").value(review.getCreatedAt().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(review.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].movieExternalId").value(createReviewRequest.getMovieExternalId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].movieTitle").value(movie.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rating").value(review.getRating()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].overview").value(review.getOverview()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(user.getId()));
    }

    @Test
    void shouldNotFindReviewsByMovieExternalIdWhenMovieIsInvalid() throws Exception {
        Mockito.when(reviewService.findReviewsByMovieExternalId(Mockito.any(Long.class))).thenThrow(new ResourceNotFoundException("Movie not found for external ID"));

        mockMvc.perform(
                MockMvcRequestBuilders.
                        get("/api/v1/reviews/movies/" + movie.getExternalId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Movie not found for external ID"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.method").value("GET"));
    }

}
