package backend.sculptor.global.exception;

import backend.sculptor.global.api.APIBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import java.rmi.ServerException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400
    @ExceptionHandler(BadRequestException.class)
    public APIBody<?> handleBadRequestException(BadRequestException e) {
        return e.getErrorResponse();
    }

    // 401
    @ExceptionHandler(SessionExpiredException.class)
    public APIBody<?> handleSessionExpiredException(SessionExpiredException e) {
        return e.getErrorResponse();
    }

    // 404
    @ExceptionHandler(NotFoundException.class)
    public APIBody<?> handleNotFoundException(NotFoundException e) {
        return e.getErrorResponse();
    }

    // 500
    @ExceptionHandler(ResourceAccessException.class)
    public APIBody<?> handleNetworkException(ResourceAccessException e) {
        return APIBody.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "네트워크 연결 실패", null);
    }

    // 500
    @ExceptionHandler(ServerException.class)
    public APIBody<?> handleServerException(ServerException e) {
        return APIBody.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류 발생", null);
    }
}
