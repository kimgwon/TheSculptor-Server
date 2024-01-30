package backend.sculptor.domain.stone.controller;

import backend.sculptor.domain.stone.dto.StoneDTO;
import backend.sculptor.domain.stone.service.StoneService;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.global.api.APIBody;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StoneController {

    private final StoneService stoneService;

    //[공방] 돌 전체 반환
    @GetMapping("/stones")
    public APIBody<List<StoneDTO>> getStones(@CurrentUser SessionUser user, @RequestParam(required = false) String category){
        List<StoneDTO> stones = stoneService.getStonesByCategory(user.getName(),category);
        return APIBody.of(200,"돌 정보 조회 성공",stones);


        //실패랑 카테고리 별 분기 처야함
    }

}
