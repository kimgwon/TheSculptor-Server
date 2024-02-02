package backend.sculptor.domain.stone.controller;

import backend.sculptor.domain.stone.dto.StoneCreateRequest;
import backend.sculptor.domain.stone.dto.StoneListDTO;
import backend.sculptor.domain.stone.entity.Category;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.service.StoneService;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.global.api.APIBody;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StoneController {

    private final StoneService stoneService;

    //[GET] 돌 전체 조회
    @GetMapping("/stones")
    public APIBody<List<StoneListDTO>> getStones(@CurrentUser SessionUser user, @RequestParam(required = false) Category category){
        if(user == null){
            //사용자 인증 실패
            return APIBody.of(401, "인증되지 않은 사용자입니다.", null);
        }try{
            List<StoneListDTO> stones = stoneService.getStonesByCategory(user.getId(), category);
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
    //[POST] 돌 생성하기
    @PostMapping("/workplace/create")
    public APIBody<StoneListDTO> createStone(@RequestBody StoneCreateRequest request) {
        try {
            //유효성 검사
            if (request == null || !request.isValid()) {
                return APIBody.of(400, "잘못된 요청 데이터입니다.", null);
            }
            StoneListDTO newStone = stoneService.createStone(request);
            if (newStone == null) {

                //돌 생성 실패 (서비스 로직 내 실패)
                return APIBody.of(500, "돌을 생성하지 못했습니다.", null);
            }
            //돌 생성 성공
            return APIBody.of(200, "돌 생성 성공", newStone);
        } catch (Exception e) {
            //기타 서버 오류
            return APIBody.of(500, "서버 오류 발생: " + e.getMessage(), null);
        }
    }


}
