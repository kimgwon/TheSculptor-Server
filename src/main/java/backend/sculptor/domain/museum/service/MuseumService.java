package backend.sculptor.domain.museum.service;

import backend.sculptor.domain.follow.service.FollowService;
import backend.sculptor.domain.museum.dto.MuseumDTO;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.repository.StoneRepository;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.global.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MuseumService {

    private final UserRepository userRepository;
    private final StoneRepository stoneRepository;
    private final FollowService followService;

    @Autowired
    public MuseumService(UserRepository userRepository, StoneRepository stoneRepository, FollowService followService) {

        this.userRepository = userRepository;
        this.stoneRepository = stoneRepository;
        this.followService = followService;
    }

    public MuseumDTO getMuseumInfo(UUID ownerId, UUID userID) {
        Users owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Stone> stones = stoneRepository.findByUsersId(ownerId);

        MuseumDTO museumDTO = new MuseumDTO();

        museumDTO.setIsOwner(userID);
        museumDTO.setOwnerId(ownerId);
        museumDTO.setOwnerNickname(owner.getNickname());
        museumDTO.setOwnerIntroduction(owner.getUserIntroduction());
        museumDTO.setOwnerProfileImage(owner.getProfileImage());
        museumDTO.setFollowerCount(followService.getFollowerSize(ownerId));
        museumDTO.setFollowingCount(followService.getFollowingSize(ownerId));

        museumDTO.setStones(convertToMuseumStones(stones));
        museumDTO.setStoneCount();

        return museumDTO;
    }

    // Stone 엔터티를 MuseumStoneDTO로 변환하는 메서드
    private List<MuseumDTO.Stone> convertToMuseumStones(List<Stone> stones) {
        return stones.stream()
                .map(this::convertToMuseumStone)
                .collect(Collectors.toList());
    }

    // 단일 Stone 엔터티를 MuseumStoneDTO로 변환하는 메서드
    private MuseumDTO.Stone convertToMuseumStone(Stone stone) {
        MuseumDTO.Stone museumStone = new MuseumDTO.Stone();
        museumStone.setId(stone.getId());
        museumStone.setName(stone.getStoneName());
        museumStone.setGoal(stone.getStoneGoal());
        museumStone.setStartDate(stone.getStartDate());
        museumStone.setDDay(stone.getFinalDate());

        return museumStone;
    }
}
