package backend.sculptor.domain.store.controller;

import backend.sculptor.domain.stone.service.ItemService;
import backend.sculptor.domain.store.dto.*;
import backend.sculptor.domain.store.service.StoreService;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.global.api.APIBody;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {
    private final ItemService itemService;
    private final StoreService storeService;

    @GetMapping("/stones")
    public APIBody<StoreStones> getStoreStones(@CurrentUser SessionUser user) {
        StoreStones storeStones = storeService.getStones(user.getId());
        return APIBody.of(HttpStatus.OK.value(), "조각상 조회 성공", storeStones);
    }

    @PatchMapping("/stones/{stoneId}/items")
    public APIBody<WearItem.Response> updateWearItem(
            @PathVariable UUID stoneId,
            @RequestBody WearItem.Request items
    ) {
        WearItem.Response wearItems = itemService.updateWearItem(stoneId, items.getItemIds());
        return APIBody.of(HttpStatus.OK.value(), "돌 아이템 착용 변경 성공", wearItems);
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

    //[GET] 돈 조회
    @GetMapping("/users/money")
    public APIBody<MoneyDTO> getTotalPowder(@CurrentUser SessionUser user) {
        if(user == null){
            //사용자 인증 실패
            return APIBody.of(401,"인증되지 않은 사용자입니다.", null);
        }try{
            int totalPowder = storeService.calculateTotalPowder(user.getId());
            MoneyDTO response = new MoneyDTO(user.getId(),totalPowder);
            return APIBody.of(200, "돈 조회 성공", response);
        }catch (Exception e){
            //기타 서버 오류
            return APIBody.of(500, "서버 오류 발생", null);
        }
    }

}
