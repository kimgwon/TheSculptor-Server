package Backend.sculptor.User.Entity;

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
    private String profile_image;
    private Boolean is_public;
    private UUID represent_stone_id;

    @Builder
    public Users(String name, String role, String nickname, String profile_image) {
        this.name = name;
        this.role = role;
        this.nickname = nickname;
        this.profile_image = profile_image;
    }
}
