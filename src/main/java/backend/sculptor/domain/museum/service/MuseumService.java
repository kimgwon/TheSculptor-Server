package backend.sculptor.domain.museum.service;

import backend.sculptor.domain.follow.service.FollowService;
import backend.sculptor.domain.museum.dto.MuseumDTO;
import backend.sculptor.domain.stone.dto.StoneListDTO;
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

        List<StoneListDTO> stones = stoneService.getStonesByUserIdAfterFinalDate(ownerId);

        MuseumDTO museum = new MuseumDTO();

        museum.setIsOwner(userID);
        museum.setIsFollowing(followService.isFollowing(userID, ownerId));
        museum.setId(ownerId);
        museum.setNickname(owner.getNickname());
        museum.setIntroduction(owner.getUserIntroduction());
        museum.setProfileImage(owner.getProfileImage());
        museum.setStones(convertToMuseumStones(stones));
        museum.setFollowerCount(followService.getFollowerSize(ownerId));
        museum.setFollowingCount(followService.getFollowingSize(ownerId));
        museum.setStoneCount();

        return museum;
    }

    // Stone 엔터티를 MuseumStoneDTO로 변환하는 메서드
    private List<MuseumDTO.Stone> convertToMuseumStones(List<StoneListDTO> stones) {
        return stones.stream()
                .map(this::convertToMuseumStone)
                .toList();
    }

    // 단일 Stone 엔터티를 MuseumStoneDTO로 변환하는 메서드
    private MuseumDTO.Stone convertToMuseumStone(StoneListDTO stone) {
        MuseumDTO.Stone museumStone = new MuseumDTO.Stone();
        LocalDateTime startDate = stone.getStartDate();

        museumStone.setId(stone.getStoneId());
        museumStone.setName(stone.getStoneName());
        museumStone.setStartDate(startDate);
        museumStone.setDDay(stoneService.calculateDate(startDate.toLocalDate()));

        return museumStone;
    }
}
