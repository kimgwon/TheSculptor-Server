package backend.sculptor.global.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}

