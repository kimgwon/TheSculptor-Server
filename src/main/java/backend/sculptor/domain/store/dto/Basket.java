package backend.sculptor.domain.store.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

public final class Basket {
    @Getter
    public static final class Request {
        private List<UUID> itemIds;
    }

    @Getter
    @Builder
    public static final class Response {
        private UUID stoneId;
        private List<Item> items;
        private List<Type> types;

        @Getter
        @Builder
        public static final class Item {
            private UUID id;
            private Integer price;
            private Boolean isPurchased;
        }

        @Getter
        @Builder
        public static final class Type {
            private UUID id;
            private Integer price;
            private Boolean isPurchased;
        }
    }
}
