package backend.sculptor.global.exception;

import backend.sculptor.global.api.APIBody;
import io.micrometer.common.lang.Nullable;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final transient APIBody<Nullable> errorResponse;

    public NotFoundException(String message) {
        super(message);
        this.errorResponse = APIBody.of(404, message, null);
    }
}
