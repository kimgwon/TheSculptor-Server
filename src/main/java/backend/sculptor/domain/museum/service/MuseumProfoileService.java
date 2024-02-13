package backend.sculptor.domain.museum.service;

import backend.sculptor.domain.museum.dto.MuseumProfileDTO;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.service.StoneService;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.global.exception.BadRequestException;
import backend.sculptor.global.exception.ErrorCode;
import backend.sculptor.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MuseumProfoileService {
    private final UserRepository userRepository;
    private final StoneService stoneService;

    public MuseumProfileDTO.User getProfileUser(UUID userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        return MuseumProfileDTO.User.builder()
                .nickname(user.getNickname())
                .introduction(user.getIntroduction())
                .profileImage(user.getProfileImage())
                .build();
    }

    public List<MuseumProfileDTO.Stone> getProfileStones(UUID userId) {
        List<Stone> stones = stoneService.getStonesByUserIdAfterFinalDate(userId);

        return convertToMuseumProfileStones(stones);
    }

    // Stone 엔터티를 MuseumStoneDTO로 변환하는 메서드
    private List<MuseumProfileDTO.Stone> convertToMuseumProfileStones(List<Stone> stones) {
        return stones.stream()
                .map(this::convertToMuseumProfileStone)
                .toList();
    }

    // 단일 Stone 엔터티를 MuseumStoneDTO로 변환하는 메서드
    private MuseumProfileDTO.Stone convertToMuseumProfileStone(Stone stone) {
        LocalDateTime startDate = stone.getStartDate();

        return MuseumProfileDTO.Stone.builder()
                .id(stone.getId())
                .name(stone.getStoneName())
                .dDay(stoneService.calculateDate(startDate.toLocalDate()))
                .build();
    }

    public MuseumProfileDTO.User editProfile(UUID userId, MuseumProfileDTO.User newProfile) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        String nickname = newProfile.getNickname();
        String introduction = newProfile.getIntroduction();
        String profileImage = newProfile.getProfileImage();

        if (nickname != null) {
            user.setNickname(nickname);
        }

        if (introduction != null) {
            user.setIntroduction(introduction);
        }

        if (profileImage != null) {
            user.setProfileImage(profileImage);
        }

        Users editUser = userRepository.save(user);

        return MuseumProfileDTO.User.builder()
                .nickname(editUser.getNickname())
                .introduction(editUser.getIntroduction())
                .profileImage(editUser.getProfileImage())
                .build();
    }

    @Transactional
    public void deleteStone(UUID userId, UUID stoneId) {
        Stone stone = stoneService.getStoneByUserIdAndStoneId(userId, stoneId);

        if (stone.getFinalDate().isAfter(LocalDateTime.now())) {
            throw new BadRequestException(ErrorCode.STONE_NOT_COMPLETE.getMessage());
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        if (stone.equals(user.getRepresentStone()))
            user.setRepresentStone(null);

        stoneService.delete(stone);
    }
}
