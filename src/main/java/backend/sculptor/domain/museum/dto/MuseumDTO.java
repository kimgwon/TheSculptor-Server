package backend.sculptor.domain.museum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class MuseumDTO {

    private Boolean isOwner;
    private Boolean isFollowing;
    private UUID id;
    private String nickname;
    private String introduction;
    private String profileImage;
    private Integer stoneCount;
    private Integer followerCount;
    private Integer followingCount;

    private List<Stone> stones;

    @Getter
    @Builder
    public static final class Stone {
        private UUID id;
        private String name;
        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDateTime startDate;
        private String dDay;

        @JsonProperty("dDay")
        public String getdDay(){
            return dDay;
        }
    }
}

