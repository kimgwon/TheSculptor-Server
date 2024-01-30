package Backend.sculptor.Stone.Controller;

import Backend.sculptor.APIResponse.APIBody;
import Backend.sculptor.OAuth.Annotation.CurrentUser;
import Backend.sculptor.Stone.DTO.StoneDTO;
import Backend.sculptor.Stone.Entity.Stone;
import Backend.sculptor.Stone.Service.StoneService;
import Backend.sculptor.User.Entity.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class StoneController {

    private final StoneService stoneService;

    @GetMapping("/stones")
    public APIBody<List<StoneDTO>> getStones(@CurrentUser SessionUser user, @RequestParam(required = false) String category){
        List<StoneDTO> stones = stoneService.getStonesByCategory(user.getName(),category);
        return APIBody.of(200,"돌 정보 조회 성공",stones);


        //실패랑 카테고리 별 분기 처야함
    }

}
