package backend.sculptor.domain.museum.dto;

import backend.sculptor.domain.comment.dto.CommentDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Builder
public final class MuseumDetailDTO {
    private Stone stone;

    @Getter
    @Builder
    public static final class Stone {
        private UUID id;
        private String name;
        private String category;
        private String goal;
        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDateTime startDate;
        private String oneComment;
        private String dDay;
        private Integer powder;
        private Boolean isRepresent;
        private Boolean isLike;
        private Long achievementRate;
        private Map<String, Long> achievementCounts;

        @JsonProperty("dDay")
        public String getdDay(){
            return dDay;
        }
    }

    private List<CommentDTO.Info> comments;
}
