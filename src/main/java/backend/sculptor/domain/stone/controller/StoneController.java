package backend.sculptor.domain.stone.controller;

import backend.sculptor.domain.museum.dto.MuseumProfileDTO;
import backend.sculptor.domain.stone.dto.StoneCreateRequest;
import backend.sculptor.domain.stone.dto.StoneDetailDTO;
import backend.sculptor.domain.stone.dto.StoneListDTO;
import backend.sculptor.domain.stone.entity.Category;
import backend.sculptor.domain.stone.service.StoneService;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.global.api.APIBody;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
                return APIBody.of(200,"조회된 돌 정보가 없습니다.", Collections.emptyList());
            }
            return APIBody.of(200, "돌 정보 조회 성공", stones);

        }catch (Exception e){
            //기타 서버 오류
            return APIBody.of(500, "서버 오류 발생"+e.getMessage(),null);
        }
    }

    //[GET] 돌 하나씩 조회
    @GetMapping("/stones/{stoneId}")
    public APIBody<StoneDetailDTO> getOneStone(@CurrentUser SessionUser user, @PathVariable UUID stoneId) {
        if (user == null) {
            // 사용자 인증 실패
            return APIBody.of(401, "인증되지 않은 사용자입니다.", null);
        }
        try {
            StoneDetailDTO stone = stoneService.getStoneByStoneId(user.getId(), stoneId);
            if (stone == null) {
                // 해당 ID를 가진 돌을 찾을 수 없는 경우
                return APIBody.of(404, "해당 돌 정보를 찾을 수 없습니다.", null);
            }
            return APIBody.of(200, "돌 정보 조회 성공", stone);
        } catch (Exception e) {
            // 기타 서버 오류
            return APIBody.of(500, "서버 오류 발생: " + e.getMessage(), null);
        }
    }

    // [DELETE] 돌 하나 삭제
    @DeleteMapping("/stones/{stoneId}/delete")
    public APIBody<MuseumProfileDTO.User> deleteStone(
            @CurrentUser SessionUser user,
            @PathVariable UUID stoneId) {
        stoneService.deleteStone(user.getId(), stoneId);
        return APIBody.of(200, "공방 돌 삭제 성공", null);
    }

    //[POST] 돌 생성하기
    @PostMapping("/workplace/create")
    public APIBody<StoneListDTO> createStone(@CurrentUser SessionUser user,@RequestBody StoneCreateRequest request) {
        if (user == null) {
            // 사용자 인증 실패
            return APIBody.of(401, "인증되지 않은 사용자입니다.", null);
        }
        try {
            //유효성 검사
            if (request == null || !request.isValid()) {
                return APIBody.of(400, "잘못된 요청 데이터입니다.", null);
            }
            StoneListDTO newStone = stoneService.createStone(user.getId(),request);
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

    //[POST] 이끼 제거하기
    @PostMapping("/stones/{stoneId}/removeMoss")
    public APIBody<?> removeMoss(@CurrentUser SessionUser user, @PathVariable UUID stoneId) {
        if (user == null) {
            // 사용자 인증 실패
            return APIBody.of(401, "인증되지 않은 사용자입니다.", null);
        }
        try {
            stoneService.removeMoss(stoneId);
            return APIBody.of(200, "이끼가 성공적으로 제거되었습니다.", null);

        } catch (IllegalStateException e) {

            // 돌 상태가 이끼(MOSS)가 아닌 경우
            return APIBody.of(400, e.getMessage(), null);

        } catch (Exception e) {
            // 기타 서버 오류
            return APIBody.of(500, "서버 오류 발생: " + e.getMessage(), null);
        }
    }

    //[POST] 균열 메꾸기
    @PostMapping("/stones/{stoneId}/repairCrack")
    public APIBody<?> repairCrack(@CurrentUser SessionUser user, @PathVariable UUID stoneId) {
        if (user == null) {
            // 사용자 인증 실패
            return APIBody.of(401, "인증되지 않은 사용자입니다.", null);
        }
        try {
            stoneService.repairCrack(stoneId);
            return APIBody.of(200, "균열이 성공적으로 메워졌습니다.", null);

        } catch (IllegalStateException e) {

            // 돌 상태가 실금(S_CRACK) 또는 균열(L_CRACK)가 아닌 경우
            return APIBody.of(400, e.getMessage(), null);

        } catch (Exception e) {
            // 기타 서버 오류
            return APIBody.of(500, "서버 오류 발생: " + e.getMessage(), null);
        }
    }

}
