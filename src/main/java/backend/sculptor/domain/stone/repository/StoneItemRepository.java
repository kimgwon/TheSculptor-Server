package backend.sculptor.domain.stone.repository;

import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.entity.StoneItem;

import java.util.List;

public interface StoneItemRepository extends BaseStoneProductRepository<StoneItem> {
    List<StoneItem> findAllByStone(Stone stone);
}
