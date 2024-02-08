package backend.sculptor.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CommentLikeDTO {
    private UUID commentId;
    private Boolean isLike;
}
