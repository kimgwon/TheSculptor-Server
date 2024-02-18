package backend.sculptor.domain.store.dto;

import backend.sculptor.domain.stone.entity.Category;
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
        private Category category;
        private Integer powder;
    }
}
