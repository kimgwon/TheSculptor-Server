package backend.sculptor.domain.museum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class MuseumDTO {

    private Boolean isOwner;
    private Boolean isFollowing;
    private UUID id;
    private String nickname;
    private String introduction;
    private String profileImage;
    private int stoneCount;
    private int followerCount;
    private int followingCount;

    private List<Stone> stones;

    @Getter
    @Setter
    public static class Stone {
        private UUID id;
        private String name;
        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDateTime startDate;
        @JsonProperty(value = "dDay")
        private String dDay;
    }

    public void setIsOwner(UUID userId) { this.isOwner = this.id == userId; }
    public void setStoneCount() {
        this.stoneCount = stones != null ? stones.size() : 0;
    }
}

