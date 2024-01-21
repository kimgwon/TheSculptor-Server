package Backend.sculptor.OAuth.MemberInfo;

public interface OAuth2MemberInfo {
    String getProviderId();
    String getProvider();
    String getName();
    String getEmail();
    String getProfileImage();
}
