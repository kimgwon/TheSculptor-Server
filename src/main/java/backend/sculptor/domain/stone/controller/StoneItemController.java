package backend.sculptor.domain.stone.controller;

import backend.sculptor.domain.stone.entity.Item;
import backend.sculptor.domain.stone.entity.StoneItem;
import backend.sculptor.domain.stone.service.StoneService;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.global.api.APIBody;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class StoneItemController {
    private final StoneService stoneService;

    @GetMapping("/store/stones/{stoneId}")
    public ResponseEntity<APIBody<Object>> showStoneItems(@CurrentUser SessionUser user,
                                          @PathVariable("stoneId") UUID stoneId) {
        APIBody<Object> response = null;
        if (user == null) {
            response = APIBody.of(400, "사용자 정보가 존재하지 않습니다.", null);
            return ResponseEntity.badRequest().body(response);
        }
        try {
            List<StoneItem> stoneItems = stoneService.findStoneItems(stoneId);
            List<Map<String, Object>> itemsList = new ArrayList<>();

            for (StoneItem stoneItem : stoneItems) {
                Item item = stoneItem.getItem();
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("itemId", item.getId());
                itemData.put("itemName", item.getItemName());
                itemData.put("itemPrice", item.getItemPrice());
                itemsList.add(itemData);
            }

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("stoneId", stoneId);
            responseData.put("items", itemsList);

            return ResponseEntity.ok(APIBody.of(200, "조각상이 착용중인 아이템 조회 성공", responseData));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIBody.of(500, "서버 에러: " + e.getMessage(), null));
        }
    }
}
