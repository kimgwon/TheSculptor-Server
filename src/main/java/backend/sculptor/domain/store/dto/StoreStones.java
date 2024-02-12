package backend.sculptor.domain.store.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public final class StoreStones {
    private List<Stone> stones;

    @Getter
    @Builder
    public static final class Stone {
        private UUID id;
        private String name;
        private Integer powder;
    }
}
