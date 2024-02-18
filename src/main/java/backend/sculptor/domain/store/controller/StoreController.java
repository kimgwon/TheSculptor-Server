package backend.sculptor.domain.store.controller;

import backend.sculptor.domain.stone.entity.Item;
import backend.sculptor.domain.stone.service.ItemService;
import backend.sculptor.domain.store.dto.*;
import backend.sculptor.domain.store.service.StoreService;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.global.api.APIBody;
import backend.sculptor.global.exception.ErrorCode;
import backend.sculptor.global.exception.NotFoundException;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {
    private final UserRepository userRepository;
    private final ItemService itemService;
    private final StoreService storeService;

    @GetMapping("/stones")
    public APIBody<StoreStones> getStoreStones(@CurrentUser SessionUser user) {
        StoreStones storeStones = storeService.getStones(user.getId());
        return APIBody.of(HttpStatus.OK.value(), "조각상 조회 성공", storeStones);
    }

    @PatchMapping("/stones/{stoneId}/type")
    public APIBody<WearType.Response> updateWearType(
            @CurrentUser SessionUser user,
            @PathVariable UUID stoneId,
            @RequestParam(required = false) UUID id
    ) {
        WearType.Response wearType = itemService.updateWearType(user.getId(), stoneId, id);
        return APIBody.of(HttpStatus.OK.value(), "돌 원석 착용 변경 성공", wearType);
    }


    @GetMapping("/stones/{stoneId}/basket")
    public APIBody<Basket.Response> getStoreBasket(
            @CurrentUser SessionUser user,
            @PathVariable UUID stoneId,
            @RequestBody Basket.Request basket
    ) {
        Basket.Response storeStones = storeService.getBasketItems(user.getId(), stoneId, basket.getItemIds());
        return APIBody.of(HttpStatus.OK.value(), "장바구니 정보 조회 성공", storeStones);
    }

    @PostMapping("/stones/{stoneId}/purchase")
    public APIBody<Purchase.Response> purchaseItems(
            @CurrentUser SessionUser user,
            @PathVariable UUID stoneId,
            @RequestBody Purchase.Request items
    ) {
        Purchase.Response purchaseItems = storeService.purchaseItems(user.getId(), stoneId, items.getItemIds());
        return APIBody.of(HttpStatus.OK.value(), "아이템 구매 성공", purchaseItems);
    }


    @GetMapping("/items")
    public ResponseEntity<APIBody<?>> showItems() {
        try {
            List<Item> items = storeService.findItems();
            List<Map<String, Object>> itemsList = new ArrayList<>();

            for (Item item : items) {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("item_id", item.getId());
                itemData.put("item_name", item.getItemName());
                itemData.put("item_price", item.getItemPrice());
                itemsList.add(itemData);
            }

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("items", itemsList);

            return ResponseEntity.ok(APIBody.of(200, "상점의 아이템 조회 성공", responseData));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(APIBody.of(400, "조회 가능한 아이템이 없습니다.", null));
        }
    }

    //[GET] 돈 조회
    @GetMapping("/users/money")
    public APIBody<MoneyDTO> getTotalPowder(@CurrentUser SessionUser currentUser) {
        if (currentUser == null) {
            //사용자 인증 실패
            return APIBody.of(401, "인증되지 않은 사용자입니다.", null);
        }
        try {
            Users user = userRepository.findById(currentUser.getId())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

            MoneyDTO response = new MoneyDTO(user.getId(), user.getTotalPowder());
            return APIBody.of(200, "돈 조회 성공", response);
        } catch (Exception e) {
            //기타 서버 오류
            return APIBody.of(500, "서버 오류 발생", null);

        }
    }

    //user가 가진 stone의 id -> stone_item 에서 검색 -> item_id로 item 테이블에 id,name,price
    @GetMapping("/users/items")
    public APIBody<?> getUsersItems(@CurrentUser SessionUser user) {
        if (user == null) {
            return APIBody.of(401, "인증되지 않은 사용자입니다.", null);
        }
        try {
            List<StoreItemDTO> userItems = storeService.findUserItems(user.getId());
            Map<String, List<StoreItemDTO>> data = new HashMap<>();
            data.put("userItems", userItems);
            return APIBody.of(200, "사용자가 구매한 아이템 조회 성공", data);
        } catch (Exception e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            return APIBody.of(500, "서버 내부 오류", null);
        }
    }

    @GetMapping("/stones/{stoneId}")
    public ResponseEntity<APIBody<Object>> showStoneItems(@CurrentUser SessionUser user,
                                                          @PathVariable("stoneId") UUID stoneId) {
        APIBody<Object> response = null;
        if (user == null) {
            response = APIBody.of(400, "사용자 정보가 존재하지 않습니다.", null);
            return ResponseEntity.badRequest().body(response);
        }
        try {
            List<StoreItemDTO> stoneItems = storeService.findStoneItems(stoneId);
            Map<String, List<StoreItemDTO>> data = new HashMap<>();
            data.put("stoneItems", stoneItems);

            return ResponseEntity.ok(APIBody.of(200, "조각상이 착용중인 아이템 조회 성공", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIBody.of(500, "서버 에러: " + e.getMessage(), null));
        }
    }
}
