package backend.sculptor.domain.user.controller;

import backend.sculptor.domain.stone.dto.StoneListDTO;
import backend.sculptor.domain.stone.entity.Item;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.service.StoneService;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.domain.user.service.UserService;
import backend.sculptor.global.api.APIBody;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import backend.sculptor.global.oauth.memberInfo.KakaoMemberInfo;
import backend.sculptor.global.oauth.memberInfo.KakaoProfile;
import backend.sculptor.global.oauth.memberInfo.OAuth2MemberInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final StoneService stoneService;
    private final ObjectMapper objectMapper;
    private final HttpSession httpSession;

    @GetMapping("/mypage")
    public ResponseEntity<APIBody<Map<String, Object>>> showMyPage(@CurrentUser SessionUser user) {
        if (user == null) {
            return ResponseEntity.badRequest().body(APIBody.of(400, "사용자 정보가 없습니다.", null));
        }

        try {
            Users findUser = userRepository.findById(user.getId()).orElseThrow(NoSuchElementException::new);

            Map<String, Object> userData = new HashMap<>();
            userData.put("user_id", findUser.getId());
            userData.put("user_name", findUser.getName());
            userData.put("profile_image", findUser.getProfileImage());
            userData.put("nickname", findUser.getNickname());
            userData.put("is_public", findUser.getIsPublic());

            return ResponseEntity.ok(APIBody.of(200, "마이페이지 조회 성공", userData));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIBody.of(404, "해당 사용자가 존재하지 않습니다.", null));
        }
    }


    @PatchMapping("/mypage/rename")
    public ResponseEntity<APIBody<Map<String, Object>>> updateNickname(@CurrentUser SessionUser user, @RequestBody Map<String, String> request) {
        String newNickname = request.get("nickname");

        if (user == null) {
            return ResponseEntity.badRequest().body(APIBody.of(401, "사용자 정보가 없습니다.", null));
        }

        try {
            Users findUser = userRepository.findById(user.getId()).orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
            userService.updateNickname(findUser, newNickname);

            Map<String, String> updatedFields = new HashMap<>();
            updatedFields.put("nickname", newNickname);

            return ResponseEntity.ok(APIBody.of(200, "개인정보 수정 성공", Map.of("updatedFields", updatedFields)));

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIBody.of(404, "해당 사용자가 없습니다.", null));
        }
    }


    @PatchMapping("/mypage/private")
    public ResponseEntity<APIBody<Map<String, Object>>> togglePublic(@CurrentUser SessionUser user, @RequestBody Map<String, Boolean> request) {
        Boolean isPublic = request.get("is_public");

        if (user == null) {
            return ResponseEntity.badRequest().body(APIBody.of(401, "사용자 정보가 없습니다.", null));
        }

        try {
            Users findUser = userRepository.findById(user.getId()).orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
            userService.updatePublic(findUser, isPublic);

            Map<String, Object> updatedFields = new HashMap<>();
            updatedFields.put("is_public", isPublic);

            Map<String, Object> data = new HashMap<>();
            data.put("updatedFields", updatedFields);

            return ResponseEntity.ok(APIBody.of(200, "공개 여부 전환 성공", data));

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIBody.of(404, "해당 사용자가 존재하지 않습니다.", null));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<APIBody<Object>> deleteUser(@CurrentUser SessionUser user) {
        if (user == null) {
            return ResponseEntity.badRequest().body(APIBody.of(401, "사용자 정보가 없습니다.", null));
        }

        try {
            Users findUser = userRepository.findById(user.getId()).orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
            userService.deleteUser(findUser);

            return ResponseEntity.ok(APIBody.of(200, "회원 탈퇴 성공", null));

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIBody.of(404, "해당 사용자가 존재하지 않습니다.", null));
        }
    }

    @GetMapping("/mypage/purchases")
    public ResponseEntity<APIBody<Map<String, Object>>> purchaseList(@CurrentUser SessionUser user) {
        if (user == null) {
            return ResponseEntity.badRequest().body(APIBody.of(401, "사용자 정보가 없습니다.", null));
        }

        Users findUser = userService.findUser(user.getId());
        List<StoneListDTO> stones = stoneService.getStonesByCategory(user.getId(), null);
        //List<Stone> stones = findUser.getStones();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("user_id", user.getId().toString());

        if (stones == null || stones.isEmpty()) {
            responseData.put("items", Collections.emptyList());
            return ResponseEntity.ok(APIBody.of(200, "구매한 아이템이 없습니다.", responseData));
        } else {
            List<Map<String, Object>> items = stones.stream()
                    .flatMap(stone -> stoneService.findAllStoneItem(stone).stream())
                    .map(stoneItem -> {
                        Item item = stoneItem.getItem();
                        Map<String, Object> itemData = new HashMap<>();
                        itemData.put("item_id", item.getId().toString());
                        itemData.put("item_name", item.getItemName());
                        itemData.put("item_price", item.getItemPrice());
                        return itemData;
                    })
                    .collect(Collectors.toList());

            responseData.put("items", items);
            return ResponseEntity.ok(APIBody.of(200, "구매한 아이템 목록 조회 성공.", responseData));
        }
    }

    @GetMapping("/user-logout")
    public ResponseEntity<APIBody<String>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            System.out.println("로그아웃 session = " + session.getId());
            session.invalidate();
            APIBody<String> response = APIBody.of(200, "로그아웃 성공", null);
            return ResponseEntity.ok(response);
        } else {
            APIBody<String> response = APIBody.of(400, "세션이 존재하지 않음", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PatchMapping("/user/represent-stone/{stoneId}")
    public APIBody<Map<String, UUID>> setRepresentStone(
            @CurrentUser SessionUser user,
            @PathVariable UUID stoneId) {
        Stone stone = stoneService.getStoneByUserIdAndStoneId(user.getId(), stoneId);
        UUID representStoneId = userService.setRepresentStone(user.getId(), stone);
        Map<String, UUID> data = new HashMap<>();
        data.put("id", representStoneId);

        return APIBody.of(HttpStatus.OK.value(), "대표 돌 설정 성공", data);
    }

    //액세스 토큰을 받았을때 사용자를 로그인 시키고, 세션에 담아 세션 아이디를 리턴
    @GetMapping("/user/login")
    public ResponseEntity<APIBody<?>> userLogin(HttpServletRequest request, RestTemplate restTemplate) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(APIBody.of(400,"Authorization header is missing or invalid",null));
        }
        String accessToken = authorizationHeader.substring(7);
        System.out.println("accessToken = " + accessToken);

        // 카카오 사용자 정보 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        // HttpEntity 객체 생성
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        try {
            // 카카오 사용자 정보 요청
            ResponseEntity<String> response = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, entity, String.class);
            KakaoProfile profile = objectMapper.readValue(response.getBody(), KakaoProfile.class);

            Optional<Users> findMember = userRepository.findByName(profile.getProperties().getNickname());
            Users users;
            if (findMember.isEmpty()) {
                users = Users.builder()
                        .name(profile.getProperties().getNickname()) // "properties" 내의 "nickname"
                        .role("ROLE_USER")
                        .nickname("kakao_" + profile.getId())
                        .profileImage(profile.getProperties().getProfileImage())
                        .build();
                userRepository.save(users);
            } else {
                users = findMember.get();
            }
            httpSession.setAttribute("user", new SessionUser(users));
            //httpSession.setMaxInactiveInterval(1800);
            httpSession.setMaxInactiveInterval(-1);
            // 성공 응답 반환
            return ResponseEntity.ok(APIBody.of(200, "로그인 요청 성공", Map.of("sessionId", httpSession.getId(), "userId", users.getId())));
        } catch (HttpClientErrorException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(APIBody.of(e.getStatusCode().value(), "사용자 정보 요청 실패", null));
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}