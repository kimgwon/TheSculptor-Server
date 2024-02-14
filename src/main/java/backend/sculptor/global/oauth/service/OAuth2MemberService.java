package backend.sculptor.global.oauth.service;

import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.global.oauth.PrincipalDetails;
import backend.sculptor.global.oauth.memberInfo.GoogleMemberInfo;
import backend.sculptor.global.oauth.memberInfo.KakaoMemberInfo;
import backend.sculptor.global.oauth.memberInfo.OAuth2MemberInfo;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2MemberService extends DefaultOAuth2UserService {
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2MemberInfo memberInfo = null;

        String accessToken = userRequest.getAccessToken().getTokenValue();
        //System.out.println("ㄹㅇaccessToken = " + accessToken);

        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            memberInfo = new GoogleMemberInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            memberInfo = new KakaoMemberInfo(oAuth2User.getAttributes());
        } else {
            System.out.println("로그인 실패");
        }

        String provider = memberInfo.getProvider();
        String providerId = memberInfo.getProviderId();
        String nickname = provider + "_" + providerId; //중복이 발생하지 않도록 provider와 providerId를 조합
        String username = memberInfo.getName();

        //String email = memberInfo.getEmail();
        String profile_image = memberInfo.getProfileImage();
        String role = "ROLE_USER"; //일반 유저
        Users users = null;
        return new PrincipalDetails(users, oAuth2User.getAttributes());
    }
}