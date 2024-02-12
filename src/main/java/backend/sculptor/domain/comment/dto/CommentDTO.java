package backend.sculptor.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

public final class CommentDTO {
    @Getter
    @Builder
    public static class Info {
        private UUID id;
        private UUID writerId;
        private String writerNickname;
        private String writerProfileImage;
        private String content;
        private Boolean isLike;
        private Integer likeCount;
        private String writeAt;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        private String content;
    }

    @Getter
    @Builder
    public static class Response {
        private UUID id;
        private UUID stoneId;
        private UUID writerId;
        private String content;
        private LocalDateTime writeAt;
    }

}

