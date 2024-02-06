package backend.sculptor.domain.follow.service;

import backend.sculptor.domain.follow.dto.FollowSimpleListDto;
import backend.sculptor.domain.follow.entity.Follow;
import backend.sculptor.domain.follow.repository.FollowRepository;
import backend.sculptor.domain.stone.dto.StoneDetailDTO;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.repository.StoneRepository;
import backend.sculptor.domain.stone.service.AchieveService;
import backend.sculptor.domain.stone.service.StoneService;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final StoneRepository stoneRepository;
    private final FollowRepository followRepository;
    private final StoneService stoneService;
    private final AchieveService achieveService;
    private final UserService userService;
    public List<FollowSimpleListDto> getFollowingList(UUID userId){
        List<Follow> followList = followRepository.findAllByFromUser(userId);
        List<FollowSimpleListDto> followingDtoList = new ArrayList<>();
        for (Follow follow : followList) {
            UUID followingUserId = follow.getToUser(); // 팔로우하는 사용자의 ID
            Users findUser = userService.findUser(followingUserId);
            String nickname = findUser.getNickname(); // 팔로우하는 사용자의 닉네임
            UUID representStoneId = findUser.getRepresentStoneId(); // 대표 돌의 ID

            // DTO 객체 생성
            FollowSimpleListDto dto = new FollowSimpleListDto(followingUserId, nickname, representStoneId);

            // DTO 리스트에 추가
            followingDtoList.add(dto);
        }
        for (FollowSimpleListDto dto : followingDtoList) {
            System.out.println(dto);
        }
        return followingDtoList;
    }

    public StoneDetailDTO searchStone(UUID representStoneId) {
        Stone stone = stoneRepository.findById(representStoneId).orElseThrow(NoSuchElementException::new);
        String dDay = stoneService.calculateDate(stone.getStartDate().toLocalDate());
        long achievementRate = achieveService.calculateAchievementRate(stone.getId());

        StoneDetailDTO stoneInfo = new StoneDetailDTO(stone.getId(),
                stone.getStoneName(),
                stone.getCategory(),
                stone.getStoneGoal(),
                stone.getStartDate(),
                dDay,
                achievementRate,
                stone.getPowder(),
                stone.getStoneLike()
                );
        return stoneInfo;
    }

    public UUID follow(UUID currentUserId, UUID followId) {
        Follow follow = new Follow(followId, currentUserId);
        followRepository.save(follow);
        return followId;
    }
}
