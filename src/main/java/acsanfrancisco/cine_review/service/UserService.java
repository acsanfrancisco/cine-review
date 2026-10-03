package acsanfrancisco.cine_review.service;

import acsanfrancisco.cine_review.dto.request.CreateUserRequest;
import acsanfrancisco.cine_review.dto.response.UserResponse;
import acsanfrancisco.cine_review.entity.User;
import acsanfrancisco.cine_review.exception.ResourceAlreadyExistsException;
import acsanfrancisco.cine_review.exception.ResourceNotFoundException;
import acsanfrancisco.cine_review.exception.UserException;
import acsanfrancisco.cine_review.mapper.UserMapper;
import acsanfrancisco.cine_review.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        if(userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists: "  + createUserRequest.getEmail());
        }
        User user = UserMapper.toEntity(createUserRequest);
        return UserMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void  deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for ID: " + userId));
        if(!user.getReviews().isEmpty()){
            throw new UserException("Must not delete a User with Reviews");
        }
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for ID: " + userId));
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for Email: " + email));
    }

}
