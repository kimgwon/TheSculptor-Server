package Backend.sculptor.Member.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Users {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String role;
    private String nickname;
    private String profile_image;

    @Builder
    public Users(String name, String role, String nickname, String profile_image) {
        this.name = name;
        this.role = role;
        this.nickname = nickname;
        this.profile_image = profile_image;
    }
}
