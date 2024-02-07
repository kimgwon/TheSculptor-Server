package backend.sculptor.global.exception;

import backend.sculptor.global.api.APIBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;
import java.rmi.ServerException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<APIBody<?>> handleNetworkException(ConnectException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIBody.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "네트워크 연결에 문제가 발생했습니다.", null));
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<APIBody<?>> handleServerException(ServerException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIBody.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버에서 오류가 발생했습니다.", null));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<APIBody<Nullable>> handleException(Exception e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new APIBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
//    }

    @ExceptionHandler(SessionExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<APIBody<Nullable>> handleSessionExpiredException(SessionExpiredException e) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 세션 만료 여부 확인
        boolean isSessionExpired = authentication == null || !authentication.isAuthenticated();

        if (isSessionExpired) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIBody.of(HttpStatus.UNAUTHORIZED.value(), "세션이 만료되었습니다.", null));
        }

        APIBody<Nullable> errorResponse = APIBody.of(404, e.getMessage(), null);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<APIBody<Nullable>> handleNotFoundException(NotFoundException e) {
        APIBody<Nullable> errorResponse = APIBody.of(404, e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
