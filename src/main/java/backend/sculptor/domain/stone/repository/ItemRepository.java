package backend.sculptor.domain.stone.repository;

import backend.sculptor.domain.stone.entity.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends BaseProductRepository<Item> {
}
