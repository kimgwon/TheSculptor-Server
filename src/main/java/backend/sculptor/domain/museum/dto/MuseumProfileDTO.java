package backend.sculptor.domain.museum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

public class MuseumProfileDTO {

    @Getter
    @Builder
    public static class User {
        private String nickname;
        private String profileImage;
        private String introduction;
    }

    @Getter
    @Builder
    public static class Stone {
        private UUID id;
        private String name;
        private String dDay;
        @JsonProperty("dDay")
        public String getdDay(){
            return dDay;
        }
    }
}
