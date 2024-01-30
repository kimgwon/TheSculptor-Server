package Backend.sculptor.User.Entity;


import lombok.Getter;

@Getter
public class SessionUser {
    private String name;
    private String profileImage;

    public SessionUser(Users user) {
        this.name = user.getName();
        this.profileImage = user.getProfileImage();
    }
}
