package backend.sculptor.domain.home.controller;

import backend.sculptor.domain.follow.dto.FollowSimpleListDto;
import backend.sculptor.domain.follow.entity.Follow;
import backend.sculptor.domain.follow.repository.FollowRepository;
import backend.sculptor.domain.follow.service.FollowService;
import backend.sculptor.domain.stone.dto.StoneDTO;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.service.StoneService;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.domain.user.service.UserService;
import backend.sculptor.global.api.APIBody;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final FollowService followService;
    private final UserService userService;
    private final StoneService stoneService;

    @GetMapping("/followings/stones")
    public ResponseEntity<?> showAllFollowingsStones(@CurrentUser SessionUser user) {
        List<FollowSimpleListDto> followingList = followService.getFollowingList(user.getId());
        List<Map<String, Object>> followerStonesList = new ArrayList<>();
        for (FollowSimpleListDto followSimpleListDto : followingList) {
            StoneDTO stoneDTO = followService.searchStone(followSimpleListDto.getRepresentStoneId());
            Map<String, Object> followerStoneMap = new HashMap<>();
            followerStoneMap.put("id", followSimpleListDto.getId());
            followerStoneMap.put("nickname", followSimpleListDto.getNickname());
            //followerStoneMap.put("stoneDDay", stoneDTO.getStoneDDay());
            // 달성률 데이터 추가 예정
            followerStoneMap.put("stoneName", stoneDTO.getStoneName());
            followerStoneMap.put("stoneGoal", stoneDTO.getStoneGoal());
            //followerStoneMap.put("startDate", stoneDTO.getStartDate());
            // 이하 isLike, like 데이터 추가
            //followerStoneMap.put("isLike", stoneDTO.getIsLike()); // 예시 값
            //followerStoneMap.put("like", stoneDTO.getLikes); // 예시 값

            followerStonesList.add(followerStoneMap);
        }
        APIBody<List<Map<String, Object>>> response = APIBody.of(200, "모든 팔로잉 사용자의 모든 돌 조회 성공", followerStonesList);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/followings/{userId}/stones/{stoneId}")
//    public ResponseEntity<?> showFollowingsStones(@CurrentUser SessionUser user,
//                                                  @PathVariable("userId") UUID userId,
//                                                  @PathVariable("stoneId") UUID stoneId) {
//        Users findUser = userService.findUser(userId);
//        StoneDTO stone = stoneService.findStone(stoneId);
//
//        List<Map<String, Object>> stoneInfo = new ArrayList<>();
//        Map<String, Object> stoneDetails = new HashMap<>();
//        stoneDetails.put("userId", findUser.getId());
//        stoneDetails.put("userName", findUser.getName());
//        // 이하 stone의 상세 정보 추가
//        stoneDetails.put("stoneId", stone.getId());
//        stoneDetails.put("stoneName", stone.getName());
//        stoneDetails.put("stoneDDay", stone.getDDay());
//        stoneDetails.put("stoneGoal", stone.getGoal());
//        stoneDetails.put("stoneStartDate", stone.getStartDate());
//
//        stoneInfo.add(stoneDetails);
//
//        APIBody<List<Map<String, Object>>> responseBody = APIBody.of(200, "친구 조각상 조회 성공", stoneInfo);
//        return ResponseEntity.ok(responseBody);
//    }
}
