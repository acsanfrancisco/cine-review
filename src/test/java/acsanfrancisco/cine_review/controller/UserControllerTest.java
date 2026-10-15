package acsanfrancisco.cine_review.controller;

import acsanfrancisco.cine_review.dto.request.CreateUserRequest;
import acsanfrancisco.cine_review.dto.response.UserResponse;
import acsanfrancisco.cine_review.entity.User;
import acsanfrancisco.cine_review.exception.ResourceAlreadyExistsException;
import acsanfrancisco.cine_review.exception.ResourceNotFoundException;
import acsanfrancisco.cine_review.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockitoBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    UserResponse userResponse;

    User user;

    String json = """
                {
                    "fullName" : "Little Zé da Silva",
                    "email" : "zezinho@gmail.com"
                }
                """;

    @BeforeEach
    void setUp(){
        user =  new User();
        user.setId(1L);
        user.setFullName("Little Zé da Silva");
        user.setEmail("zezinho@gmail.com");

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setFullName("Little Zé da Silva");
        userResponse.setEmail("zezinho@gmail.com");

    }

    @Test
    @DisplayName("Should create user when request is valid")
    void shouldCreateUser() throws Exception {
        Mockito.when(userService.createUser(Mockito.any(CreateUserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userResponse.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value(userResponse.getFullName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userResponse.getEmail()));
    }

    @Test
    @DisplayName("Should throw Resource Already Exists Exception - 409")
    void shouldNotCreateUserWhenEmailIsDuplicated() throws Exception {
        Mockito.when(userService.createUser(Mockito.any(CreateUserRequest.class))).thenThrow(new ResourceAlreadyExistsException("Email already exists"));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(409))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusText").value("Conflict"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email already exists"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.method").value("POST"));
    }

    @Test
    @DisplayName("Should delete User by ID")
    void shouldDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(Mockito.anyLong());

       mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/v1/users/" + user.getId())
       )
               .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Should throw Resource Not Found Exception - 404")
    void shouldNotDeleteUserWhenUserNotFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("User not found for ID")).when(userService).deleteUser(Mockito.anyLong());

        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/v1/users/" + user.getId())
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusText").value("Not Found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found for ID"));
    }

    @Test
    @DisplayName("Should return User by ID")
    void shouldFindUserById() throws Exception {
        Mockito.when(userService.findUserById(Mockito.any(Long.class))).thenReturn(user);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value(user.getFullName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    @DisplayName("Should throw Resource Not Found Exception - 404")
    void shouldNotFindUserByIdWhenUserNotFound() throws Exception {
        Mockito.when(userService.findUserById(Mockito.any(Long.class))).thenThrow(new ResourceNotFoundException("User not found for ID"));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found for ID"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.method").value("GET"));
    }

    @Test
    @DisplayName("Should return User by email")
    void shouldFindUserByEmail() throws Exception {
        Mockito.when(userService.findUserByEmail(Mockito.any(String.class))).thenReturn(user);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("email", userResponse.getEmail())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userResponse.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value(userResponse.getFullName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userResponse.getEmail()));
    }

    @Test
    @DisplayName("Should throw Resource Not Found Exception - 404")
    void shouldNotFindUserByEmailWhenUserNotFound() throws Exception {
        Mockito.when(userService.findUserByEmail(Mockito.any(String.class))).thenThrow(new ResourceNotFoundException("User not found for Email"));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("email", userResponse.getEmail())
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found for Email"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.method").value("GET"));
    }

}
