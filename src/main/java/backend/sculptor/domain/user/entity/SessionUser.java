package backend.sculptor.domain.user.entity;


import lombok.Getter;

import java.util.UUID;

@Getter
public class SessionUser {
    private String name;
    private String profileImage;
    private UUID id;

    public SessionUser(Users user) {
        this.id = user.getId();
        this.name = user.getName();
        this.profileImage = user.getProfileImage();
    }
}
