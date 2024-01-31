package backend.sculptor.domain.user.entity;

import backend.sculptor.domain.stone.entity.Stone;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy="users")
    private final List<Stone> stones = new ArrayList<>();

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
