package acsanfrancisco.cine_review.mapper;

import acsanfrancisco.cine_review.dto.request.CreateUserRequest;
import acsanfrancisco.cine_review.dto.response.UserResponse;
import acsanfrancisco.cine_review.entity.User;

public class UserMapper {

    public static User toEntity(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setEmail(createUserRequest.getEmail());
        user.setFullName(createUserRequest.getFullName());
        user.setReviews(null);
        return user;
    }

    public static UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFullName(user.getFullName());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }
}
