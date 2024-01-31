package backend.sculptor.domain.stone.controller;

import backend.sculptor.domain.stone.dto.StoneListDTO;
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
    public APIBody<List<StoneListDTO>> getStones(@CurrentUser SessionUser user, @RequestParam(required = false) String category){
        if(user == null){
            //사용자 인증 실패
            return APIBody.of(401, "인증되지 않은 사용자입니다.", null);
        }try{
            List<StoneListDTO> stones = stoneService.getStonesByCategory(user.getName(), category);
            if (stones.isEmpty()){
                //조회된 데이터가 없는 경우
                return APIBody.of(404,"조회된 돌 정보가 없습니다.", null);
            }
            return APIBody.of(200, "돌 정보 조회 성공", stones);

        }catch (Exception e){
            //기타 서버 오류
            return APIBody.of(500, "서버 오류 발생"+e.getMessage(),null);
        }
    }

}
