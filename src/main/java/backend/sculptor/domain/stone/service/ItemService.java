package backend.sculptor.domain.stone.service;

import backend.sculptor.domain.stone.entity.Item;
import backend.sculptor.domain.stone.entity.StoneItem;
import backend.sculptor.domain.stone.repository.ItemRepository;
import backend.sculptor.domain.stone.repository.StoneItemRepository;
import backend.sculptor.domain.store.dto.WearItem;
import backend.sculptor.global.exception.ErrorCode;
import backend.sculptor.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final StoneItemRepository stoneItemRepository;

    @Transactional
    public WearItem.Response updateWearItem(UUID stoneId, List<UUID> itemIds) {
        List<StoneItem> stoneItems = stoneItemRepository.findAllByStoneId(stoneId);
        List<WearItem.Response.StoneItem> items = stoneItems.stream()
                .map(stoneItem -> {
                    UUID itemId = stoneItem.getItem().getId();
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

    public Boolean isPurchasedItem(UUID stoneId, UUID itemId){
        return stoneItemRepository.findByStoneIdAndItemId(stoneId, itemId).isPresent();
    }

    public List<Item> getItemsById(List<UUID> itemsId) {
        return itemsId.stream()
                .map(this::getItemById)
                .collect(Collectors.toList());
    }

    public Item getItemById(UUID itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ITEM_NOT_FOUND.getMessage()));
    }
}
