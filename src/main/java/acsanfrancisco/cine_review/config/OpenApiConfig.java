package acsanfrancisco.cine_review.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI () {
       return new OpenAPI()
               .info(new Info()
                       .title("Cine Review API - Movie Review API")
                       .description("""
                               Cine Review is a REST API for managing users, movies, and movie reviews.
                               The application integrates with the TMDB(The Movie DataBase) API to search and retrieve
                               movie information from an external source. Movie data fetched through TMDB can be used to
                               identify and associate movies in the application, while users can create and manage reviews
                               for movies available in the system.
                               The API provides endpoints for user management, movie search and persistence, and review
                               management, following a layered architecture with DTOs, mappers, validation, exception handler,
                               and integration with external services.
                               The project was developed with Spring Boot to demonstrate the practical application of
                               software development concepts and design patterns in a backend application.
                               This product uses the TMDB API but it is not endorsed or certified by TMDB.
                               """)
                       .version("v1.0.0")
                       .contact(new Contact()
                               .name("João Francisco Silva Mendes Santiago")
                               .email("joaosannetof@gmail.com")
                               .url("https://github.com/acsanfrancisco"))
                       .license(new License()
                               .name("The Movie Database (TMDB)")
                               .url("https://www.themoviedb.org"))
               );
    }
}
