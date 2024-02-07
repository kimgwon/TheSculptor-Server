package backend.sculptor.global.exception;

import backend.sculptor.global.api.APIBody;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends RuntimeException {
    private final transient APIBody<?> errorResponse;

    public BadRequestException(String message) {
        super(message);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        this.errorResponse = APIBody.of(status.value(), message, null);
    }
}
