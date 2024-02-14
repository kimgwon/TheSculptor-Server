package backend.sculptor.domain.museum.service;

import backend.sculptor.domain.follow.service.FollowService;
import backend.sculptor.domain.museum.dto.MuseumDTO;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.service.StoneService;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.global.exception.ErrorCode;
import backend.sculptor.global.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MuseumService {

    private final UserRepository userRepository;
    private final StoneService stoneService;
    private final FollowService followService;

    public MuseumDTO getMuseumInfo(UUID ownerId, UUID userID) {
        Users owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        List<Stone> stones = stoneService.getStonesByUserIdAfterFinalDate(ownerId);

        return MuseumDTO.builder()
                .isOwner(isOwner(ownerId, userID))
                .isFollowing(followService.isFollowing(userID, ownerId))
                .id(ownerId)
                .nickname(owner.getNickname())
                .introduction(owner.getIntroduction())
                .profileImage(owner.getProfileImage())
                .stones(convertToMuseumStones(stones))
                .followerCount(followService.getFollowerSize(ownerId))
                .followingCount(followService.getFollowingSize(ownerId))
                .stoneCount(getStoneCount(stones))
                .build();
    }

    // Stone 엔터티를 MuseumStoneDTO로 변환하는 메서드
    private List<MuseumDTO.Stone> convertToMuseumStones(List<Stone> stones) {
        return stones.stream()
                .map(this::convertToMuseumStone)
                .toList();
    }

    // 단일 Stone 엔터티를 MuseumStoneDTO로 변환하는 메서드
    private MuseumDTO.Stone convertToMuseumStone(Stone stone) {
        LocalDateTime startDate = stone.getStartDate();

        return MuseumDTO.Stone.builder()
                .id(stone.getId())
                .name(stone.getStoneName())
                .startDate(startDate)
                .dDay(stoneService.calculateDate(startDate.toLocalDate()))
                .build();
    }

    public boolean isOwner(UUID ownerId, UUID userId) { return ownerId.equals(userId); }
    public int getStoneCount(List<Stone> stones) {
        return stones != null ? stones.size() : 0;
    }
}
