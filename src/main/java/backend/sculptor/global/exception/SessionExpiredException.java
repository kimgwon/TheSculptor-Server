package backend.sculptor.global.exception;

import backend.sculptor.global.api.APIBody;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SessionExpiredException extends RuntimeException {
    private final transient APIBody<?> errorResponse;

    public SessionExpiredException(String message) {
        super(message);
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        this.errorResponse = APIBody.of(status.value(), message, null);
    }
}
