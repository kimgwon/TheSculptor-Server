package Backend.sculptor.User.Entity;


import lombok.Getter;

import java.util.UUID;

@Getter
public class SessionUser {
    private String name;
    private String profile_image;
    private UUID id;

    public SessionUser(Users user) {
        this.id = user.getId();
        this.name = user.getName();
        this.profile_image = user.getProfile_image();
    }
}
