package backend.sculptor.global.exception;

import backend.sculptor.global.api.APIBody;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends RuntimeException {
    private final transient APIBody<?> errorResponse;

    public NotFoundException(String message) {
        super(message);
        HttpStatus status = HttpStatus.NOT_FOUND;
        this.errorResponse = APIBody.of(status.value(), message, null);
    }
}
