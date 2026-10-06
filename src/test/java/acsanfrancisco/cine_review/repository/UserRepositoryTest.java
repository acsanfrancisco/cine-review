package acsanfrancisco.cine_review.repository;

import acsanfrancisco.cine_review.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void shouldSaveUser() {
        User user = new User();
        user.setFullName("Little Zé da Silva");
        user.setEmail("zezinho@gmail.com");

        userRepository.save(user);

        Assertions.assertNotNull(user.getId());
        Optional<User> foundUser = userRepository.findById(user.getId());
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(user.getFullName(), foundUser.get().getFullName());
        Assertions.assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void shouldNotSaveUserWithNullFullName() {
        User user = new User();
        user.setFullName(null);
        user.setEmail("zezinho@gmail.com");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user);
        });
    }

    @Test
    void shouldNotSaveUserWithNullEmail() {
        User user = new User();
        user.setFullName("Little Zé da Silva");
        user.setEmail(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user);
        });
    }

    @Test
    void shouldNotSaveUserWithDuplicatedEmail() {
        User user = new User();
        user.setFullName("Little Zé da Silva");
        user.setEmail("zezinho@gmail.com");
        userRepository.saveAndFlush(user);

        User duplicatedEmail = new User();
        duplicatedEmail.setFullName("Little Zé da Silva Junior");
        duplicatedEmail.setEmail("zezinho@gmail.com");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(duplicatedEmail);
        });
    }

    @Test
    void shouldDeleteUser(){
        User user = new User();
        user.setFullName("Little Zé da Silva");
        user.setEmail("zezinho@gmail.com");

        userRepository.save(user);
        Assertions.assertNotNull(user.getId());

        userRepository.deleteById(user.getId());
        Assertions.assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    void shouldFindUserById(){
        User user = new User();
        user.setFullName("Little Zé da Silva");
        user.setEmail("zezinho@gmail.com");

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getId());

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(user.getId(), foundUser.get().getId());
        Assertions.assertEquals(user.getFullName(), foundUser.get().getFullName());
        Assertions.assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    @Sql("/sql/seeding-users.sql")
    void shouldNotFindUserById(){
        Optional<User> user = userRepository.findById(4L);
        Assertions.assertFalse(user.isPresent());
    }

    @Test
    @Sql("/sql/seeding-users.sql")
    void shouldFindUserByEmail(){
        Optional<User> user = userRepository.findByEmail("zezinho@gmail.com");
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals("Zé da Silva",  user.get().getFullName());
        Assertions.assertEquals("zezinho@gmail.com", user.get().getEmail());
    }

    @Test
    @Sql("/sql/seeding-users.sql")
    void shouldNotFindUserByEmail(){
        Optional<User> user = userRepository.findByEmail("invalido@gmail.com");
        Assertions.assertFalse(user.isPresent());
    }

    @Test
    @Sql("/sql/seeding-users.sql")
    void shouldReturnTrueToExistsByEmail(){
        boolean exists = userRepository.existsByEmail("zezinho@gmail.com");
        Assertions.assertTrue(exists);
    }

    @Test
    @Sql("/sql/seeding-users.sql")
    void shouldReturnFalseToExistsByEmail(){
        boolean exists = userRepository.existsByEmail("invalido@gmail.com");
        Assertions.assertFalse(exists);
    }
}
