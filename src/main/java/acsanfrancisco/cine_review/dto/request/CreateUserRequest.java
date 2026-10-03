package acsanfrancisco.cine_review.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CreateUserRequest {

    @NotBlank(message = "User full name must be informed")
    @Length(min = 5, max = 255, message = "Min length = 5 / Max length = 255")
    private String fullName;

    @NotBlank(message = "User email must be informed")
    @Email(message = "Provide a valid email")
    private String email;

}
