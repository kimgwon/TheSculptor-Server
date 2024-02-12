package backend.sculptor.domain.store.service;

import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.service.StoneService;
import backend.sculptor.domain.store.dto.StoreStones;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.global.exception.ErrorCode;
import backend.sculptor.global.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {
    private final UserRepository userRepository;
    private final StoneService stoneService;

    public StoreStones getStones(UUID userID) {
        Users user = userRepository.findById(userID)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        List<Stone> stones = stoneService.getStonesByUserIdAfterFinalDate(user.getId());

        return StoreStones.builder()
                .stones(convertToStoreStones(stones))
                .build();
    }

    // Stone 엔터티를 MuseumStoneDTO로 변환하는 메서드
    private List<StoreStones.Stone> convertToStoreStones(List<Stone> stones) {
        return stones.stream()
                .map(this::convertToStoreStone)
                .toList();
    }

    // 단일 Stone 엔터티를 MuseumStoneDTO로 변환하는 메서드
    private StoreStones.Stone convertToStoreStone(Stone stone) {
        return StoreStones.Stone.builder()
                .id(stone.getId())
                .name(stone.getStoneName())
                .powder(stone.getPowder())
                .build();
    }
}
