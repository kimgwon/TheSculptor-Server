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
import backend.sculptor.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final StoneRepository stoneRepository;
    private final FollowRepository followRepository;
    private final StoneService stoneService;
    private final AchieveService achieveService;
    private final UserService userService;

    public List<FollowSimpleListDto> getFollowingList(UUID userId) {
        List<Follow> followings = followRepository.findAllByFromUserId(userId);

        return convertToDtoList(followings);
    }

    public List<FollowSimpleListDto> getFollowerList(UUID userId) {
        List<Follow> followers = followRepository.findAllByToUserId(userId);

        return convertToDtoList(followers);
    }

    public Integer getFollowingSize(UUID userId){
        return getFollowingList(userId).size();
    }

    public Integer getFollowerSize(UUID userId){
        return getFollowerList(userId).size();
    }

    private List<FollowSimpleListDto> convertToDtoList(List<Follow> followList) {
        List<FollowSimpleListDto> dtoList = new ArrayList<>();

        for (Follow follow : followList) {
            UUID followingUserId = follow.getToUser().getId();
            Users findUser = userService.findUser(followingUserId);

            if (findUser != null) {
                String nickname = findUser.getNickname();
                String profileImage = findUser.getProfileImage();
                UUID representStoneId = findUser.getRepresentStoneId();

                FollowSimpleListDto dto = new FollowSimpleListDto(followingUserId, nickname, profileImage, representStoneId);
                dtoList.add(dto);
            }
        }

        return dtoList;
    }

    @Transactional
    public StoneDetailDTO searchStone(UUID representStoneId) {
        Stone stone = stoneRepository.findById(representStoneId).orElseThrow(NoSuchElementException::new);
        String dDay = stoneService.calculateDate(stone.getStartDate().toLocalDate());
        long achievementRate = achieveService.calculateAchievementRate(stone.getId());

        return new StoneDetailDTO(
                stone.getId(),
                stone.getStoneName(),
                stone.getCategory(),
                stone.getStoneGoal(),
                stone.getStartDate(),
                dDay,
                achievementRate,
                stone.getPowder(),
                stone.getStatus(),
                stone.getLikes().size()
        );
    }

    public Boolean toggleFollow(Users fromUser, Users toUser) {
        Optional<Follow> existingFollow = followRepository.findByFromUserIdAndToUserId(fromUser.getId(), toUser.getId());

        // 해당 사용자가 이미 팔로우한 경우 언팔로우
        if (existingFollow.isPresent()) {
            followRepository.delete(existingFollow.get());
            return false;
        } else { // 팔로우하지 않은 경우 팔로우
            Follow follow = new Follow(toUser, fromUser);
            followRepository.save(follow);
            return true;
        }
    }

    public boolean isFollowing(UUID fromUserId, UUID toUserId) {
        return followRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId);
    }
}
