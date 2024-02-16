package backend.sculptor.domain.store.service;

import backend.sculptor.domain.stone.entity.Item;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.entity.StoneItem;
import backend.sculptor.domain.stone.repository.StoneItemRepository;
import backend.sculptor.domain.stone.repository.StoneRepository;
import backend.sculptor.domain.stone.service.ItemService;
import backend.sculptor.domain.stone.service.StoneService;
import backend.sculptor.domain.store.dto.*;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.global.exception.BadRequestException;
import backend.sculptor.global.exception.ErrorCode;
import backend.sculptor.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final UserRepository userRepository;
    private final StoneItemRepository stoneItemRepository;
    private final StoneService stoneService;
    private final ItemService itemService;
    private final StoneRepository stoneRepository;

    public StoreStones getStones(UUID userID) {
        Users user = userRepository.findById(userID)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        List<Stone> stones = stoneService.getStonesByUserIdAfterFinalDate(user.getId());

        return StoreStones.builder()
                .stones(convertToStoreStones(stones))
                .build();
    }

    @Transactional
    public Basket.Response getBasketItems(UUID userId, UUID stoneId, List<UUID> itemsID){
        List<Item> items = itemService.getItemsById(itemsID);
        Stone stone = stoneService.getStoneByUserIdAndStoneId(userId, stoneId);

        return Basket.Response.builder()
                .stoneId(stone.getId())
                .items(convertToBasketResponse(stoneId, items))
                .build();
    }

    @Transactional
    public Purchase.Response purchaseItems(UUID userId, UUID stoneId, List<UUID> itemIds){
        // TODO : 사용자의 powder가 구매할 아이템의 파우더보다 적으면 예외처리

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));
        Stone stone = stoneService.getStoneByUserIdAndStoneId(userId, stoneId);
        List<Purchase.Response.StoneItem> items = itemIds.stream()
                .map(itemId -> {
                    boolean isPurchasedItem = itemService.isPurchasedItem(stoneId, itemId);
                    if (isPurchasedItem) {
                        throw new BadRequestException(ErrorCode.STONE_ALREADY_PURCHASE.getMessage() + " Item Id:" + itemId);
                    } else {
                        Item item = itemService.getItemById(itemId);
                        StoneItem stoneItem = StoneItem.builder()
                                .stone(stone)
                                .item(item)
                                .build();
                        stoneItemRepository.save(stoneItem);

                        user.updateTotalPowder(-item.getItemPrice());

                        return Purchase.Response.StoneItem.builder()
                                .id(stoneItem.getItem().getId())
                                .build();
                    }
                })
                .toList();

        return Purchase.Response.builder()
                .stoneId(stone.getId())
                .items(items)
                .build();
    }

    public List<Item> findItems() {
        return itemService.findItems();
    }

    // Stone 엔터티를 StoreStones.Stone 변환하는 메서드
    private List<StoreStones.Stone> convertToStoreStones(List<Stone> stones) {
        return stones.stream()
                .map(this::convertToStoreStone)
                .toList();
    }

    // 단일 Stone 엔터티를 StoreStones.Stone 변환하는 메서드
    private StoreStones.Stone convertToStoreStone(Stone stone) {
        return StoreStones.Stone.builder()
                .id(stone.getId())
                .name(stone.getStoneName())
                .powder(stone.getPowder())
                .build();
    }

    private List<Basket.Response.Item> convertToBasketResponse(UUID stoneId, List<Item> items) {
        return items.stream()
                .map(item -> Basket.Response.Item.builder()
                        .id(item.getId())
                        .price(item.getItemPrice())
                        .isPurchased(itemService.isPurchasedItem(stoneId, item.getId()))
                        .build())
                .collect(Collectors.toList());

    }

    //돈 조회
    public int calculateTotalPowder(UUID userId){
        List<Stone> stones = stoneRepository.findByUsersId(userId);
        if (stones.isEmpty()) {
            throw new RuntimeException("사용자가 생성한 돌이 없습니다.");
        }
        return stones.stream().mapToInt(Stone::getPowder).sum();
    }

    @Transactional
    public List<StoreItemDTO> findUserItems(UUID userId) {
        Users loggedInUser = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        List<Stone> stones = loggedInUser.getStones();

        Set<UUID> uniqueItemIds = new HashSet<>();
        
        List<StoreItemDTO> uniqueItems = stones.stream()
                .flatMap(stone -> stoneItemRepository.findAllByStoneId(stone.getId()).stream())
                .filter(stoneItem -> uniqueItemIds.add(stoneItem.getItem().getId())) // 중복 제거
                .map(stoneItem -> convertToStoreItemDTO(stoneItem)) // StoneItem을 StoreItemDTO로 변환
                .collect(Collectors.toList());

        return uniqueItems;
    }

    private StoreItemDTO convertToStoreItemDTO(StoneItem stoneItem) {
        return new StoreItemDTO(
                stoneItem.getItem().getId(),
                stoneItem.getItem().getItemName(),
                stoneItem.getItem().getItemPrice()
        );
    }

    @Transactional
    public List<StoreItemDTO> findStoneItems(UUID stoneId) {
        List<StoneItem> stoneItems = stoneItemRepository.findAllByStoneId(stoneId);
        return stoneItems.stream()
                .map(this::convertToStoreItemDTO) // StoneItem을 StoreItemDTO로 변환
                .collect(Collectors.toList());
    }
}
