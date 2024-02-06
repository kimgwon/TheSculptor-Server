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
public class MuseumDTO {

    private boolean isOwner;
    private UUID ownerId;
    private String ownerNickname;
    //private String ownerIntroduction;
    private String ownerProfileImage;
    private int stoneCount;
//    private int followerCount;
//    private int followingCount;
    private List<Stone> stones;

    public void setIsOwner(UUID userId) {
        this.isOwner = this.ownerId == userId;
    }

    public void setStoneCount() {
        this.stoneCount = stones != null ? stones.size() : 0;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Stone {
        private UUID id;
        private String name;
        private String goal;
        private LocalDateTime startDate;
        private long dDay;

        // d-day 계산 메서드
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
}

