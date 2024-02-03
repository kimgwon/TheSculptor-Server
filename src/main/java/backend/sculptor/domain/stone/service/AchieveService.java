package backend.sculptor.domain.stone.service;

import backend.sculptor.domain.stone.dto.SculptorResultDTO;
import backend.sculptor.domain.stone.dto.StoneListDTO;
import backend.sculptor.domain.stone.dto.StoneSculptRequest;
import backend.sculptor.domain.stone.entity.Achieve;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.repository.AchieveRepository;
import backend.sculptor.domain.stone.repository.StoneRepository;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AchieveService {

    private final AchieveRepository achieveRepository;
    private final StoneRepository stoneRepository;
    private final UserRepository userRepository;

    //SculptorResultDTO 변환
    private SculptorResultDTO convertToResultDTO(Achieve achieve) {
        // Achieve 엔티티를 ResultDTO로 변환하는 로직
        return new SculptorResultDTO(
                achieve.getId(),
                achieve.getStone().getId(),
                achieve.getDate(),
                achieve.getAchieveStatus(),
                achieve.getStone().getPowder()
        );
    }

    //돌 조각_달성 현황 기록
    @Transactional
    public SculptorResultDTO sculptStone(UUID userId, UUID stoneId, StoneSculptRequest request) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        //현재 날짜 기준 미래 시점 요청은 예외처리
        LocalDate today = LocalDate.now();
        if (request.getDate().isAfter(today)) {
            throw new IllegalArgumentException("미래 날짜로 달성 현황을 기록할 수 없습니다.");
        }

        Stone stone = stoneRepository.findById(stoneId)
                .orElseThrow(() -> new RuntimeException("돌을 찾을 수 없습니다."));

        LocalDate requestDate = request.getDate();
        achieveRepository.findByStoneIdAndDate(stoneId, requestDate)
                .ifPresent(a -> {
                    throw new IllegalStateException("해당 날짜에 대한 기록이 이미 존재합니다.");
                });

        Achieve achieve = Achieve.builder()
                .stone(stone)
                .achieveStatus(request.getAchieveStatus())
                .date(request.getDate())
                .build();

        stone.getAchieves().add(achieve); // 돌에 달성 기록 추가
        achieveRepository.save(achieve); // 달성 기록 저장

        return convertToResultDTO(achieve);
    }


}
