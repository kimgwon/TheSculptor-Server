package backend.sculptor.domain.museum.dto;

import backend.sculptor.domain.comment.dto.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MuseumDetailDTO {

    private Stone stone;

    @Getter
    @Setter
    public static class Stone {
        private UUID id;
        private String name;
        private String category;
        private String goal;
        private LocalDateTime startDate;
        private String oneComment;
        private String dDay;
        private int powder;
        private Boolean isLike;
    }

    private List<CommentDTO.Info> comments;
}
