package backend.sculptor.domain.user.entity;

import backend.sculptor.domain.follow.entity.Follow;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.global.exception.BadRequestException;
import backend.sculptor.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Setter
    private String profileImage;
    @Setter
    private String introduction;

    @Column(nullable = false)
    private Boolean isPublic = true;

    @Setter
    private int totalPowder;
    public void updateTotalPowder(int addPowder){
        if (this.totalPowder+addPowder < 0)
            throw new BadRequestException(ErrorCode.USER_POWDER_NOT_ENOUGH.getMessage());
        this.totalPowder += addPowder;
    }

    @Setter
    @OneToOne
    @JoinColumn(name = "represent_stone_id")
    private Stone representStone;

    @OneToMany(mappedBy="users", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Stone> stones = new ArrayList<>();

    @OneToMany(mappedBy = "toUser", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Follow> following = new ArrayList<>();

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
