package backend.sculptor.global.auth.memberInfo;

public interface OAuth2MemberInfo {
    String getProviderId();
    String getProvider();
    String getName();
    String getEmail();
    String getProfileImage();
}
