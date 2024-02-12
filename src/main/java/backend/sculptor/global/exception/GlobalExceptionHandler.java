package backend.sculptor.global.exception;

import backend.sculptor.global.api.APIBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.rmi.ServerException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400
    @ExceptionHandler(BadRequestException.class)
    public APIBody<?> handleBadRequestException(BadRequestException e) {
        return e.getErrorResponse();
    }

    // 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public APIBody<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return APIBody.of(HttpStatus.BAD_REQUEST.value(), "올바르지 않은 요청 값 입니다", null);
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

    // 404
    @ExceptionHandler(NoHandlerFoundException.class)
    public APIBody<?> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return  APIBody.of(HttpStatus.NOT_FOUND.value(), "지원되지 않는 URL 입니다", null);
    }

    // 405
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public APIBody<?> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return APIBody.of(HttpStatus.METHOD_NOT_ALLOWED.value(), "지원되지 않는 요청 메서드입니다", null);
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
