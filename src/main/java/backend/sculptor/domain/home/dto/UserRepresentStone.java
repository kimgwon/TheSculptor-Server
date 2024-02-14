package backend.sculptor.domain.home.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class UserRepresentStone {
    private UUID id;
    private String name;
    private Stone stone;

    @Getter
    @Builder
    public static final class Stone {
        private UUID id;
        private String name;
        private String goal;
        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDateTime startDate;
        private String dDay;
        private Long achievementRate;

        @JsonProperty("dDay")
        public String getdDay(){
            return dDay;
        }
    }
}
