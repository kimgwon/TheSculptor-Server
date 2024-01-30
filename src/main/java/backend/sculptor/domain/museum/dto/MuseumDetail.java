package backend.sculptor.domain.museum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MuseumDetail {

    private Stone stone;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Stone {
        private UUID id;
        private String name;
        private String category;
        private String goal;
        private LocalDateTime startDate;
        private String oneComment;
        private long dDay;
        private int powder;
        private boolean isLike;

        public void setDDay(LocalDateTime finalDate) {
            if (startDate != null && finalDate != null) {
                LocalDate currentDate = LocalDate.now();
                LocalDate goalDate = finalDate.toLocalDate();
                this.dDay = ChronoUnit.DAYS.between(currentDate, goalDate);
            } else {
                this.dDay = 0; // 시작일 또는 종료일이 없는 경우 0으로 설정
            }
        }
    }

    private List<Comment> comments;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Comment {
        private UUID guestId;
        private String guestNickname;
        private String content;
        private boolean isLike;
        private LocalDateTime date;
    }
}
