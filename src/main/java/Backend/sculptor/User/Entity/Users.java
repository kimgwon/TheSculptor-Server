package Backend.sculptor.User.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Users {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String role;
    private String nickname;
    private String profileImage;

    @Column(nullable = false)
    private Boolean isPublic = true;

    private UUID representStoneId;

    @Builder
    public Users(String name, String role, String nickname, String profileImage) {
        this.name = name;
        this.role = role;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public void setNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
}
