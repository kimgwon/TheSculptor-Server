package backend.sculptor.domain.user.controller;

import backend.sculptor.domain.stone.dto.ItemDTO;
import backend.sculptor.domain.stone.entity.Item;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.entity.StoneItem;
import backend.sculptor.domain.stone.service.StoneService;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.global.api.APIBody;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final StoneService stoneService;

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
        List<Stone> stones = findUser.getStones();

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

}