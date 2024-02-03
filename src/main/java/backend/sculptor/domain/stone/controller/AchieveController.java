package backend.sculptor.domain.stone.controller;

import backend.sculptor.domain.stone.dto.SculptorResultDTO;
import backend.sculptor.domain.stone.dto.StoneSculptRequest;
import backend.sculptor.domain.stone.service.AchieveService;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.global.api.APIBody;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AchieveController {

    private final AchieveService achieveService;

    //[POST] 돌 조각_달성현황 기록하기
    @PostMapping("/stones/{stoneId}/sculpt")
    public APIBody<SculptorResultDTO> sculptStone(@CurrentUser SessionUser user, @PathVariable UUID stoneId, @RequestBody StoneSculptRequest request) {
        try {
            // 유효성 검사
            if (request == null || !request.isValid()) {
                return APIBody.of(400, "잘못된 요청 데이터입니다.", null);
            }
            SculptorResultDTO newAchieve = achieveService.sculptStone(user.getId(), stoneId, request);
            if (newAchieve == null) {

                //달성 기록 실패 (서비스 로직 내 실패)
                return APIBody.of(500, "달성 현황을 기록하지 못했습니다.", null);
            }
            //달성 현황 기록 성공
            return APIBody.of(200, "달성 현황 기록 성공", newAchieve);
        } catch (Exception e) {
            //기타 서버 오류
            return APIBody.of(500, "서버 오류 발생: " + e.getMessage(), null);
        }

    }
}
