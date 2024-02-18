package backend.sculptor.domain.stone.service;

import backend.sculptor.domain.stone.entity.Item;
import backend.sculptor.domain.stone.entity.StoneItem;
import backend.sculptor.domain.stone.repository.BaseProductRepository;
import backend.sculptor.domain.stone.repository.BaseStoneProductRepository;
import backend.sculptor.domain.stone.repository.ItemRepository;
import backend.sculptor.domain.stone.repository.StoneItemRepository;
import backend.sculptor.domain.store.dto.WearItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ItemService extends ProductService<Item, StoneItem> {
    private final ItemRepository itemRepository;
    private final StoneItemRepository stoneItemRepository;

    public ItemService(ItemRepository itemRepository, StoneItemRepository stoneItemRepository) {
        super(itemRepository, stoneItemRepository);
        this.itemRepository = itemRepository;
        this.stoneItemRepository = stoneItemRepository;
    }

    @Override
    protected BaseProductRepository<Item> getBaseProductRepository() {
        return itemRepository;
    }

    @Override
    protected BaseStoneProductRepository<StoneItem> getBaseStoneProductRepository() {
        return stoneItemRepository;
    }

    @Transactional
    public WearItem.Response updateWearItem(UUID stoneId, List<UUID> itemIds) {
        List<StoneItem> stoneItems = stoneItemRepository.findAllByStoneId(stoneId);
        List<WearItem.Response.StoneItem> items = stoneItems.stream()
                .map(stoneItem -> {
                    UUID itemId = stoneItem.getId();
                    boolean toggleWear = itemIds.contains(itemId);
                    stoneItem.setIsWorn(toggleWear);
                    stoneItemRepository.save(stoneItem);
                    return WearItem.Response.StoneItem.builder()
                            .itemId(itemId)
                            .isWorn(toggleWear)
                            .build();
                })
                .toList();

        return WearItem.Response.builder()
                .stoneId(stoneId)
                .stoneItems(items)
                .build();
    }
}
