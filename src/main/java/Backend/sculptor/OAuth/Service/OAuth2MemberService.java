package Backend.sculptor.OAuth.Service;

import Backend.sculptor.User.Entity.SessionUser;
import Backend.sculptor.OAuth.MemberInfo.GoogleMemberInfo;
import Backend.sculptor.OAuth.MemberInfo.KakaoMemberInfo;
import Backend.sculptor.User.Entity.Users;
import Backend.sculptor.User.Repository.UserRepository;
import Backend.sculptor.OAuth.MemberInfo.OAuth2MemberInfo;
import Backend.sculptor.OAuth.PrincipalDetails;
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
        System.out.println("userRequest = " + userRequest.getClientRegistration().getRegistrationId());

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
        System.out.println("memberInfo = " + memberInfo.getName());

        //String email = memberInfo.getEmail();
        String profile_image = memberInfo.getProfileImage();
        String role = "ROLE_USER"; //일반 유저

        System.out.println("oAuth2User = " + oAuth2User.getAttributes());

        Optional<Users> findMember = userRepository.findByName(username);
        Users users;
        if (findMember.isEmpty()) { //찾지 못했다면
            users = Users.builder()
                    .name(username)
                    .role(role)
                    .nickname(nickname)
                    .profile_image(profile_image)
                    .build();
            userRepository.save(users);
        } else {
            users = findMember.get();
        }

        httpSession.setAttribute("user", new SessionUser(users));
        System.out.println("users = " + users.getNickname() + users.getName());
        return new PrincipalDetails(users, oAuth2User.getAttributes());
    }
}