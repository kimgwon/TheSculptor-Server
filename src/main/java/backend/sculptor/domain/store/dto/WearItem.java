package backend.sculptor.domain.store.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

public final class WearItem {
    @Getter
    public static final class Request {
        private List<UUID> itemIds;
    }

    @Getter
    @Builder
    public static final class Response {
        private UUID stoneId;
        List<StoneItem> stoneItems;

        @Getter
        @Builder
        public static final class StoneItem{
            private UUID itemId;
            private Boolean isWorn;
        }
    }
}
