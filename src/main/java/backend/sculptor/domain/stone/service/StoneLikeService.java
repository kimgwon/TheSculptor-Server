package backend.sculptor.domain.stone.service;

import backend.sculptor.domain.stone.dto.StoneLikeDTO;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.entity.StoneLikes;
import backend.sculptor.domain.stone.repository.StoneLikesRepository;
import backend.sculptor.domain.stone.repository.StoneRepository;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.global.exception.ErrorCode;
import backend.sculptor.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoneLikeService {
    private final StoneLikesRepository stoneLikesRepository;
    private final StoneRepository stoneRepository;

    public StoneLikeDTO toggleLikeToStone(UUID stoneId, Users user) {
        Stone stone = stoneRepository.findById(stoneId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STONE_NOT_FOUND.getMessage()));

        Optional<StoneLikes> existingLike = stoneLikesRepository.findByUserIdAndStoneId(user.getId(), stoneId);

        if (existingLike.isPresent()) {
            stoneLikesRepository.delete(existingLike.get());
            return StoneLikeDTO.builder()
                    .userId(user.getId())
                    .stoneId(stoneId)
                    .isLike(false)
                    .build();
        } else {
            StoneLikes stoneLikes = new StoneLikes(stone, user);
            stoneLikesRepository.save(stoneLikes);
            return StoneLikeDTO.builder()
                    .userId(user.getId())
                    .stoneId(stoneId)
                    .isLike(true)
                    .build();
        }
    }

    public Boolean isPressedLike(UUID userId, UUID stoneId) {
        Optional<StoneLikes> byUserIdAndStoneId = stoneLikesRepository.findByUserIdAndStoneId(userId, stoneId);
        return byUserIdAndStoneId.isPresent();
    }
}
