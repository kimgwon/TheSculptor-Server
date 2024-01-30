package backend.sculptor.global.oauth.memberInfo;

public interface OAuth2MemberInfo {
    String getProviderId();
    String getProvider();
    String getName();
    String getEmail();
    String getProfileImage();
}
