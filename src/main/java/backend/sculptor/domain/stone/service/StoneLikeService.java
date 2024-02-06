package backend.sculptor.domain.stone.service;

import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.entity.StoneLikes;
import backend.sculptor.domain.stone.repository.StoneLikesRepository;
import backend.sculptor.domain.stone.repository.StoneRepository;
import backend.sculptor.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoneLikeService {
    private final StoneLikesRepository stoneLikesRepository;
    private final StoneRepository stoneRepository;

    public Boolean pressLikeToStone(UUID stoneId, UUID userId) {
        Optional<Stone> stone = stoneRepository.findById(stoneId);
        if (stone.isPresent()) {
            StoneLikes stoneLikes = new StoneLikes(stoneId, userId);
            stoneLikesRepository.save(stoneLikes);
            return true;
        } else {
            return false;
        }
    }

    public Boolean isPressedLike(UUID userId, UUID stoneId) {
        Optional<StoneLikes> byUserIdAndStoneId = stoneLikesRepository.findByUserIdAndStoneId(userId, stoneId);
        return byUserIdAndStoneId.isPresent();
    }
}
