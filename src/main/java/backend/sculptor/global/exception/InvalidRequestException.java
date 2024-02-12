package backend.sculptor.global.exception;

import backend.sculptor.global.api.APIBody;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidRequestException extends RuntimeException {
    private final transient APIBody<?> errorResponse;

    public InvalidRequestException() {
        super();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        this.errorResponse = APIBody.of(status.value(), "잘못된 요청 형식입니다.", null);
    }
}