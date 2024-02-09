package backend.sculptor.domain.stone.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class StoneLikeDTO {
    private UUID stoneId;
    private UUID userId;
    private Boolean isLike;
}
