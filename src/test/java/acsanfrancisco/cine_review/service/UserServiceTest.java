package acsanfrancisco.cine_review.service;

import acsanfrancisco.cine_review.dto.request.CreateUserRequest;
import acsanfrancisco.cine_review.dto.response.UserResponse;
import acsanfrancisco.cine_review.entity.Review;
import acsanfrancisco.cine_review.entity.User;
import acsanfrancisco.cine_review.exception.ResourceAlreadyExistsException;
import acsanfrancisco.cine_review.exception.ResourceNotFoundException;
import acsanfrancisco.cine_review.exception.UserException;
import acsanfrancisco.cine_review.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("Should save User when Create User Request is valid")
    void shouldSaveUser(){
        CreateUserRequest createUser = new CreateUserRequest();
        createUser.setFullName("Little Zé da Silva");
        createUser.setEmail("zezinho@gmail.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFullName("Little Zé da Silva");
        savedUser.setEmail("zezinho@gmail.com");

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);
        UserResponse userResponse = userService.createUser(createUser);
        Mockito.verify(userRepository).save(Mockito.any(User.class));
        Assertions.assertEquals(1L, userResponse.getId());
        Assertions.assertEquals("Little Zé da Silva", userResponse.getFullName());
        Assertions.assertEquals("zezinho@gmail.com", userResponse.getEmail());
    }

    @Test
    @DisplayName("Should throw Resource Already Exists Exception ")
    void shouldNotSaveUserIfEmailIsDuplicated(){
        CreateUserRequest user = new CreateUserRequest();
        user.setFullName("Little Zé da Silva");
        user.setEmail("zezinho@gmail.com");

        Mockito.when(userRepository.existsByEmail("zezinho@gmail.com")).thenReturn(true);
        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.createUser(user);
        });

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Should delete User when ID exists and there is no related Review")
    void shouldDeleteUser(){
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFullName("Little Zé da Silva");
        savedUser.setEmail("zezinho@gmail.com");
        savedUser.setReviews(List.of());

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        userService.deleteUser(1L);
        Mockito.verify(userRepository).delete(savedUser);
    }

    @Test
    @DisplayName("Should throw Resource Not Found Exception when User ID does not exist")
    void shouldNotDeleteUserForNonExistentId() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });
        Mockito.verify(userRepository, Mockito.never()).delete(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Should throw User Exception when trying to delete a User with Reviews")
    void shouldNotDeleteUserWithReviews(){
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFullName("Little Zé da Silva");
        savedUser.setEmail("zezinho@gmail.com");
        savedUser.setReviews(List.of(new Review()));

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        Assertions.assertThrows(UserException.class, () ->{
            userService.deleteUser(1L);
        });
        Mockito.verify(userRepository, Mockito.never()).delete(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Should return a User when ID exists")
    void shouldFindUserById(){
        User user = new User();
        user.setId(1L);
        user.setFullName("Little Zé da Silva");
        user.setEmail("zezinho@gmail.com");
        user.setReviews(List.of());

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User foundUser = userService.findUserById(1L);
        Mockito.verify(userRepository).findById(1L);

        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getFullName(), foundUser.getFullName());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    @DisplayName("Should not return a User when ID does not exist")
    void shouldNotFindUserWithNonExistentId(){
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.findUserById(1L);
        });
        Mockito.verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should return a User by email when email exists")
    void shouldFindUserByEmail(){
        User user = new User();
        user.setId(1L);
        user.setFullName("Little Zé da Silva");
        user.setEmail("zezinho@gmail.com");

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User foundUser = userService.findUserByEmail(user.getEmail());
        Mockito.verify(userRepository).findByEmail(user.getEmail());
        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getFullName(), foundUser.getFullName());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    @DisplayName("Should throw Resource Not Found Exception when email does not exist")
    void shouldNotFindUserByNonExistentEmail(){
        String email = "invalido@gmail.com";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.findUserByEmail(email);
        });
        Mockito.verify(userRepository).findByEmail(email);
    }
}
