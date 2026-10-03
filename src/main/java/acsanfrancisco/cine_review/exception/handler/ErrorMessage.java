package acsanfrancisco.cine_review.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
public class ErrorMessage {

    private String message;

    private String path;

    private String method;

    private String statusText;

    private Integer statusCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    public ErrorMessage(HttpServletRequest request, HttpStatus status, String message) {
        this.message = message;
        this.path = request.getServletPath();
        this.method = request.getMethod();
        this.statusText = status.getReasonPhrase();
        this.statusCode = status.value();
    }

    public ErrorMessage(HttpServletRequest request, HttpStatus status, String message, BindingResult result) {
        this.message = message;
        this.path = request.getServletPath();
        this.method = request.getMethod();
        this.statusText = status.getReasonPhrase();
        this.statusCode = status.value();
        addErrors(result);
    }

    private void addErrors(BindingResult result) {
        this.errors = new LinkedHashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            this.errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }


}
