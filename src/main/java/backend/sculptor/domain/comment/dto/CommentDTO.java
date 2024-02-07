package backend.sculptor.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

public class CommentDTO {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Info {
        private UUID id;
        private UUID writerId;
        private String writerNickname;
        private String content;
        private boolean isLike;
        private LocalDateTime writeAt;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        private UUID writerId;
        private String content;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private UUID id;
        private UUID stoneId;
        private UUID writerId;
        private String content;
        private LocalDateTime writeAt;
    }

}

