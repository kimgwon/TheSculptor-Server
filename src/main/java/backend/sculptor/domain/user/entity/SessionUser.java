package backend.sculptor.domain.user.entity;


import lombok.Getter;

@Getter
public class SessionUser {
    private String name;
    private String profile_image;

    public SessionUser(Users user) {
        this.name = user.getName();
        this.profile_image = user.getProfile_image();
    }
}
