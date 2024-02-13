package backend.sculptor.global.oauth.memberInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoProfile {
    private Long id;
    private String connectedAt;
    private Properties properties;

    public Long getId() {
        return id;
    }

    public String getConnectedAt() {
        return connectedAt;
    }

    public Properties getProperties() {
        return properties;
    }

    // Getter, Setter 생략
    static public class Properties {
        private String nickname;
        @JsonProperty("profile_image")
        private String profileImage;
        @JsonProperty("thumbnail_image")
        private String thumbnailImage;

        public String getNickname() {
            return nickname;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public String getThumbnailImage() {
            return thumbnailImage;
        }

        // Getter, Setter 생략
    }
}
