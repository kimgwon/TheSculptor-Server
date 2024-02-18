package backend.sculptor.domain.store.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

public final class WearType {
    @Getter
    public static final class Request {
        private UUID typeId;
    }

    @Getter
    @Builder
    public static final class Response {
        private UUID stoneId;
        private UUID typeId;
    }
}
