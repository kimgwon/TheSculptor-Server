package Backend.sculptor.User.Controller;

import Backend.sculptor.OAuth.Annotation.CurrentUser;
import Backend.sculptor.User.Entity.SessionUser;
import Backend.sculptor.User.Entity.Users;
import Backend.sculptor.User.Repository.UserRepository;
import Backend.sculptor.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/mypage")
    public ResponseEntity<Map<String, Object>> showMyPage(@CurrentUser SessionUser user) {
        if (user == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 400);
            errorResponse.put("message", "사용자 정보가 없습니다.");
            errorResponse.put("data", null);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            Users findUser = userRepository.findById(user.getId()).orElseThrow(NoSuchElementException::new);

            Map<String, Object> response = new HashMap<>();
            Map<String, Object> userData = new HashMap<>();

            userData.put("user_id", findUser.getId());
            userData.put("user_name", findUser.getName());
            userData.put("profile_image", findUser.getProfile_image());
            userData.put("nickname", findUser.getNickname());
            userData.put("is_public", findUser.getIs_public());

            response.put("code", 200);
            response.put("message", "마이페이지 조회 성공");
            response.put("data", userData);

            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 404);
            errorResponse.put("message", "해당 사용자가 존재하지 않습니다.");
            errorResponse.put("data", null);

            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @PatchMapping("/mypage/rename")
    public ResponseEntity<?> updateNickname(@CurrentUser SessionUser user, @RequestBody Map<String, String> request) {
        String newNickname = request.get("nickname");

        if (user == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 401);
            errorResponse.put("message", "사용자 정보가 없습니다.");
            errorResponse.put("data", null);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            Users findUser = userRepository.findByName(user.getName()).orElseThrow(NoSuchElementException::new);
            userService.updateNickname(findUser, newNickname);

            Map<String, Object> response = new HashMap<>();
            Map<String, String> updatedFields = new HashMap<>();
            updatedFields.put("nickname", newNickname);

            response.put("code", 200);
            response.put("message", "개인정보 수정 성공");
            response.put("data", Map.of("updatedFields", updatedFields));

            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 404);
            errorResponse.put("message", "해당 사용자가 존재하지 않습니다.");
            errorResponse.put("data", null);

            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @PatchMapping("/mypage/private")
    public ResponseEntity<?> togglePublic(@CurrentUser SessionUser user, @RequestBody Map<String, Boolean> request) {
        Boolean isPublic = request.get("is_public");

        if (user == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 401);
            errorResponse.put("message", "사용자 정보가 없습니다.");
            errorResponse.put("data", null);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            Users findUser = userRepository.findByName(user.getName()).orElseThrow(NoSuchElementException::new);
            userService.updatePublic(findUser, isPublic);

            Map<String, Object> response = new HashMap<>();
            Map<String, Boolean> updatedFields = new HashMap<>();
            updatedFields.put("is_public", isPublic);

            response.put("code", 200);
            response.put("message", "공개 여부 전환 성공");
            response.put("data", Map.of("updatedFields", updatedFields));

            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 404);
            errorResponse.put("message", "해당 사용자가 존재하지 않습니다.");
            errorResponse.put("data", null);

            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@CurrentUser SessionUser user) {
        if (user == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 401);
            errorResponse.put("message", "사용자 정보가 없습니다.");
            errorResponse.put("data", null);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            Users findUser = userRepository.findByName(user.getName()).orElseThrow(NoSuchElementException::new);
            userService.deleteUser(findUser);

            Map<String, Object> response = new HashMap<>();

            response.put("code", 200);
            response.put("message", "회원 탈퇴 성공");
            response.put("data", null);

            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 404);
            errorResponse.put("message", "해당 사용자가 존재하지 않습니다.");
            errorResponse.put("data", null);

            return ResponseEntity.status(404).body(errorResponse);
        }
    }

//    @GetMapping("/mypage/purchases")
//    public ResponseEntity<?> purchaseList(@CurrentUser SessionUser user) {
//        if (user == null) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("code", 401);
//            errorResponse.put("message", "사용자 정보가 없습니다.");
//            errorResponse.put("data", null);
//            return ResponseEntity.badRequest().body(errorResponse);
//        }
//    }
}
