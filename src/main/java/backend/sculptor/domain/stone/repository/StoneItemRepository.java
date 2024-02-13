package backend.sculptor.domain.stone.repository;

import backend.sculptor.domain.stone.dto.StoneListDTO;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.entity.StoneItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoneItemRepository extends JpaRepository<StoneItem, UUID> {
    List<StoneItem> findAllByStone(StoneListDTO stone);
    List<StoneItem> findAllByStoneId(UUID stoneId);
    Optional<StoneItem> findByStoneIdAndItemId(UUID stoneId, UUID itemId);
}
