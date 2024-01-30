package backend.sculptor.domain.museum.service;

import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.repository.StoneRepository;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.domain.museum.dto.Museum;
import backend.sculptor.global.exception.BadRequestException;
import backend.sculptor.global.exception.ExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MuseumService {

    private final UserRepository userRepository;
    private final StoneRepository stoneRepository;

    @Autowired
    public MuseumService(UserRepository userRepository, StoneRepository stoneRepository) {
        this.userRepository = userRepository;
        this.stoneRepository = stoneRepository;
    }

    public Museum getMuseumInfo(UUID ownerId, UUID userID) {
        Users owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_USER_ID));

        List<Stone> stones = stoneRepository.findByUserId(ownerId);

        Museum museum = new Museum();

        museum.setIsOwner(userID);
        museum.setOwnerId(ownerId);
        museum.setOwnerNickname(owner.getNickname());
        museum.setOwnerIntroduction(owner.getIntroduction());
        museum.setOwnerProfileImage(owner.getProfileImage());
        museum.setFollowerCount(/* 팔로워 수 */);
        museum.setFollowingCount(/* 팔로잉 수 */);

        museum.setStones(convertToMuseumStones(stones));
        museum.setStoneCount();

        return museum;
    }

    // Stone 엔터티를 MuseumStoneDTO로 변환하는 메서드
    private List<Museum.Stone> convertToMuseumStones(List<Stone> stones) {
        return stones.stream()
                .map(this::convertToMuseumStone)
                .collect(Collectors.toList());
    }

    // 단일 Stone 엔터티를 MuseumStoneDTO로 변환하는 메서드
    private Museum.Stone convertToMuseumStone(Stone stone) {
        Museum.Stone museumStone = new Museum.Stone();
        museumStone.setId(stone.getId());
        museumStone.setName(stone.getStoneName());
        museumStone.setGoal(stone.getStoneGoal());
        museumStone.setStartDate(stone.getStartDate());
        museumStone.setDDay(stone.getFinalDate());

        return museumStone;
    }
}
