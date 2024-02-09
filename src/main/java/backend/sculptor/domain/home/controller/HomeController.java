package backend.sculptor.domain.home.controller;

import backend.sculptor.domain.follow.dto.FollowSimpleListDto;
import backend.sculptor.domain.follow.service.FollowService;
import backend.sculptor.domain.stone.dto.StoneDetailDTO;
import backend.sculptor.domain.stone.service.StoneLikeService;
import backend.sculptor.domain.stone.service.StoneService;
import backend.sculptor.domain.user.dto.UserSearchResultDto;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.service.UserService;
import backend.sculptor.global.api.APIBody;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final FollowService followService;
    private final UserService userService;
    private final StoneService stoneService;
    private final StoneLikeService stoneLikeService;

    @GetMapping("/followings/stones")
    public ResponseEntity<?> showAllFollowingsStones(@CurrentUser SessionUser user) {
        APIBody<List<Map<String, Object>>> response;
        try {
            List<FollowSimpleListDto> followingList = followService.getFollowingList(user.getId());
            if (followingList.isEmpty()) { //팔로우 하는 사람이 없으면
                APIBody<List<Map<String, Object>>> noFollowing = APIBody.of(400, "팔로우 하는 사용자가 없습니다.", null);
                return ResponseEntity.badRequest().body(noFollowing);
            }
            List<Map<String, Object>> followerStonesList = new ArrayList<>();
            for (FollowSimpleListDto followSimpleListDto : followingList) {
                UUID userId = followSimpleListDto.getId();
                //대표돌 id가 null 일때..
                if (followSimpleListDto.getRepresentStoneId() == null) {
                    continue;
                }
                StoneDetailDTO stoneDetailDTO = followService.searchStone(followSimpleListDto.getRepresentStoneId());
                Boolean pressedLike = stoneLikeService.isPressedLike(userId, stoneDetailDTO.getStoneId());
                Map<String, Object> followerStoneMap = new HashMap<>();
                followerStoneMap.put("id", followSimpleListDto.getId());
                followerStoneMap.put("nickname", followSimpleListDto.getNickname());
                followerStoneMap.put("stoneDDay", stoneDetailDTO.getDDay());
                // 달성률 데이터 추가 예정
                followerStoneMap.put("stoneName", stoneDetailDTO.getStoneName());
                followerStoneMap.put("stoneGoal", stoneDetailDTO.getStoneGoal());
                followerStoneMap.put("startDate", stoneDetailDTO.getStartDate());
                // 이하 isLike, like 데이터 추가
                followerStoneMap.put("isLike", pressedLike);
                followerStoneMap.put("like", stoneDetailDTO.getLikes());

                followerStonesList.add(followerStoneMap);
            }
            response = APIBody.of(200, "모든 팔로잉 사용자의 모든 돌 조회 성공", followerStonesList);
        } catch (Exception e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            response = APIBody.of(400, "조회중 에러 발생 "+e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/followings/{userId}/stones/{stoneId}")
    public ResponseEntity<?> showFollowingsStones(@CurrentUser SessionUser user,
                                                  @PathVariable("userId") UUID userId,
                                                  @PathVariable("stoneId") UUID stoneId) {
        APIBody<List<Map<String, Object>>> responseBody;
        try {
            Users findUser = userService.findUser(userId);
            StoneDetailDTO stone = stoneService.getStoneByStoneId(userId, stoneId);

            List<Map<String, Object>> stoneInfo = new ArrayList<>();
            Map<String, Object> stoneDetails = new HashMap<>();
            stoneDetails.put("userId", findUser.getId());
            stoneDetails.put("userName", findUser.getName());
            stoneDetails.put("userProfileUrl", findUser.getProfileImage());
            // 이하 stone의 상세 정보 추가
            stoneDetails.put("stoneId", stone.getStoneId());
            stoneDetails.put("stoneName", stone.getStoneName());
            stoneDetails.put("stoneDDay", stone.getDDay());
            stoneDetails.put("stoneGoal", stone.getStoneGoal());
            stoneDetails.put("stoneStartDate", stone.getStartDate());

            stoneInfo.add(stoneDetails);
            responseBody = APIBody.of(200, "친구 조각상 조회 성공", stoneInfo);

        } catch (NoSuchElementException e) {
            responseBody = APIBody.of(400, "사용자 ID, 돌 ID 에러", null);
            return ResponseEntity.badRequest().body(responseBody);
        }

        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/users/search")
    public ResponseEntity<?> userSearch(@RequestParam String name) {
        APIBody<List<UserSearchResultDto>> responseBody;
        List<UserSearchResultDto> searchResults;
        List<Users> usersList = userService.searchUser(name);

        if (usersList.isEmpty()) { //검색 결과가 없다면
            searchResults = new ArrayList<>();
            responseBody = APIBody.of(200, "검색 결과가 없습니다.", searchResults);
            return ResponseEntity.ok(responseBody);
        } else {
            searchResults = usersList.stream()
                    .map(user -> new UserSearchResultDto(user.getId(), user.getName(), user.getProfileImage()))
                    .collect(Collectors.toList());
            responseBody = APIBody.of(200, "검색 성공.", searchResults);
            return ResponseEntity.ok(responseBody);
        }
    }

    @PostMapping("/{stoneId}/like")
    public ResponseEntity<?> pressLike(@CurrentUser SessionUser user,
                                       @PathVariable("stoneId") UUID stoneId) {
        Boolean result = stoneLikeService.pressLikeToStone(stoneId, user.getId());
        if(result) {
            Map<String, String> data = new HashMap<>();
            data.put("stoneId", stoneId.toString());
            data.put("userId", user.getId().toString());
            return ResponseEntity.ok(APIBody.of(200, "좋아요 누르기 성공", data));
        } else {
            return ResponseEntity.badRequest().body(APIBody.of(400, "좋아요 누르기 실패", null));
        }
    }
}
