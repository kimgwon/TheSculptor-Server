package backend.sculptor.domain.store.service;

import backend.sculptor.domain.stone.entity.*;
import backend.sculptor.domain.stone.service.ItemService;
import backend.sculptor.domain.stone.service.StoneService;
import backend.sculptor.domain.stone.service.TypeService;
import backend.sculptor.domain.store.dto.Basket;
import backend.sculptor.domain.store.dto.Purchase;
import backend.sculptor.domain.store.dto.StoreStones;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.global.exception.BadRequestException;
import backend.sculptor.global.exception.ErrorCode;
import backend.sculptor.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final UserRepository userRepository;
    private final StoneService stoneService;
    private final ItemService itemService;
    private final TypeService typeService;

    public StoreStones getStones(UUID userID) {
        Users user = userRepository.findById(userID)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        List<Stone> stones = stoneService.getStonesByUserIdAfterFinalDate(user.getId());

        return StoreStones.builder()
                .stones(convertToStoreStones(stones))
                .build();
    }

    @Transactional
    public Basket.Response getBasketProducts(UUID userId, UUID stoneId, List<UUID> productsID){
        List<Product> products = new ArrayList<>();

        for (UUID productId : productsID) {
            Product product = findProduct(productId);
            products.add(product);
        }

        Stone stone = stoneService.getStoneByUserIdAndStoneId(userId, stoneId);

        return Basket.Response.builder()
                .stoneId(stone.getId())
                .items(convertToBasketItemResponse(stoneId, products))
                .types(convertToBasketTypeResponse(stoneId, products))
                .build();
    }

    @Transactional
    public Purchase.Response purchaseProducts(UUID userId, UUID stoneId, List<UUID> productIds) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));
        Stone stone = stoneService.getStoneByUserIdAndStoneId(userId, stoneId);

        for (UUID productId : productIds) {
            Product product = findProduct(productId);
            if (product == null) {
                throw new BadRequestException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
            }

            boolean isPurchasedItem = itemService.isPurchased(stoneId, productId);
            boolean isPurchasedType = typeService.isPurchased(stoneId, productId);
            if (isPurchasedItem || isPurchasedType) {
                throw new BadRequestException(ErrorCode.STONE_ALREADY_PURCHASE.getMessage() + " Product Id:" + productId);
            }

            user.updateTotalPowder(-product.getPrice());

            StoneProduct stoneProduct = StoneProduct.builder()
                    .stone(stone)
                    .product(product)
                    .build();

            if (product instanceof Item) {
                itemService.purchase(StoneItem.builder()
                                .id(stoneProduct.getId())
                                .product(stoneProduct.getProduct())
                                .stone(stoneProduct.getStone())
                                .isWorn(false)
                                .build());
            } else if (product instanceof Type) {
                typeService.purchase(StoneType.builder()
                                .id(stoneProduct.getId())
                                .product(stoneProduct.getProduct())
                                .stone(stoneProduct.getStone())
                                .build());
            }
        }

        return Purchase.Response.builder()
                .stoneId(stone.getId())
                .products(productIds)
                .build();
    }

    public Product findProduct(UUID productId){
        if (itemService.isProduct(productId))
            return itemService.getProductById(productId);
        else if (typeService.isProduct(productId))
            return typeService.getProductById(productId);
        else
            throw new BadRequestException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
    }

    // Stone 엔터티를 StoreStones.Stone 변환하는 메서드
    private List<StoreStones.Stone> convertToStoreStones(List<Stone> stones) {
        if (stones.isEmpty()) {
            return null;
        }

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

    private List<Basket.Response.Item> convertToBasketItemResponse(UUID stoneId, List<Product> products) {
        if (products.isEmpty()) {
            return null;
        }

        return products.stream()
                .filter(product -> product instanceof Item)
                .map(item -> Basket.Response.Item.builder()
                        .id(item.getId())
                        .price(item.getPrice())
                        .isPurchased(itemService.isPurchased(stoneId, item.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    private List<Basket.Response.Type> convertToBasketTypeResponse(UUID stoneId, List<Product> products) {
        if (products.isEmpty()) {
            return null;
        }

        return products.stream()
                .filter(product -> product instanceof Type)
                .map(type -> Basket.Response.Type.builder()
                        .id(type.getId())
                        .price(type.getPrice())
                        .isPurchased(typeService.isPurchased(stoneId, type.getId()))
                        .build())
                .collect(Collectors.toList());
    }
}
