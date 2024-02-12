package backend.sculptor.domain.store.controller;

import backend.sculptor.domain.store.dto.StoreStones;
import backend.sculptor.domain.store.service.StoreService;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.global.api.APIBody;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {
    private final StoreService storeService;

    @GetMapping("/stones")
    public APIBody<StoreStones> getStoreStones(@CurrentUser SessionUser user) {
        StoreStones storeStones = storeService.getStones(user.getId());
        return APIBody.of(HttpStatus.OK.value(), "조각상 조회 성공", storeStones);
    }

}
