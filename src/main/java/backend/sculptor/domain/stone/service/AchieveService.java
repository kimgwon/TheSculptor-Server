package backend.sculptor.domain.stone.service;

import backend.sculptor.domain.stone.dto.AchieveDTO;
import backend.sculptor.domain.stone.dto.SculptorResultDTO;
import backend.sculptor.domain.stone.dto.StoneAchievesListDTO;
import backend.sculptor.domain.stone.dto.StoneSculptRequest;
import backend.sculptor.domain.stone.entity.Achieve;
import backend.sculptor.domain.stone.entity.AchieveStatus;
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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    //AchieveDTO 변환
    private AchieveDTO convertToAchieveDTO(Achieve achieve) {
        // Achieve 엔티티를 AchieveDTO로 변환하는 로직
        return new AchieveDTO(
                achieve.getId(),
                achieve.getDate(),
                achieve.getAchieveStatus());
    }

    //돌 달성현황 계산 로직
    public static Map<String, Long> AchievementCounts(List<Achieve> achieves) {
        // 달성 현황 계산
        Map<AchieveStatus, Long> counts = achieves.stream()
                .collect(Collectors.groupingBy(Achieve::getAchieveStatus, Collectors.counting()));

        // Map<AchieveStatus, Long>를 Map<String, Long>으로 변환
        return counts.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().name(), Map.Entry::getValue));
    }

    //돌 달성현황 계산 로직(박물관)
    public Map<String, Long> achievementCountsByStoneId(UUID stoneId) {
        List<Achieve> achieves = achieveRepository.findByStoneId(stoneId);

        // 달성 현황 계산
        Map<AchieveStatus, Long> counts = achieves.stream()
                .collect(Collectors.groupingBy(Achieve::getAchieveStatus, Collectors.counting()));

        // Map<AchieveStatus, Long>를 Map<String, Long>으로 변환
        return counts.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().name(), Map.Entry::getValue));
    }

    //돌 달성현황 전체 조회
    @Transactional(readOnly = true)
    public StoneAchievesListDTO findAllAchievesByStoneId(UUID stoneId) {
        List<Achieve> achieves = achieveRepository.findByStoneId(stoneId);

        List<AchieveDTO> achieveDTOs = achieves.stream()
                .map(this::convertToAchieveDTO)
                .collect(Collectors.toList());

        Map<String, Long> achievementCounts = AchievementCounts(achieves);

        // Stone 정보와 AchieveDTO 리스트를 포함하는 StoneAchievesDTO 반환
        return new StoneAchievesListDTO(stoneId, achievementCounts, achieveDTOs);
    }



    //돌 조각_달성 현황 기록
    @Transactional
    public SculptorResultDTO sculptStone(UUID userId, UUID stoneId, StoneSculptRequest request) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Stone stone = stoneRepository.findById(stoneId)
                .orElseThrow(() -> new RuntimeException("돌을 찾을 수 없습니다."));

        //현재 날짜 기준 미래 시점 요청은 예외처리
        LocalDate today = LocalDate.now();
        if (request.getDate().toLocalDate().isAfter(today)) {
            throw new IllegalArgumentException("미래 날짜로 달성 현황을 기록할 수 없습니다.");
        }

        // 시작일로부터 종료일 사이의 날짜만 요청 가능
        LocalDateTime requestDate = request.getDate();
        LocalDateTime startDate = stone.getStartDate();
        LocalDateTime finalDate = stone.getFinalDate();

        if (requestDate.isBefore(startDate) || requestDate.isAfter(finalDate)) {
            throw new IllegalArgumentException("요청 날짜가 유효한 범위를 벗어났습니다.");
        }

        // 동일 날짜에 대한 기록이 이미 있는지 확인
        boolean exists = achieveRepository.findByStoneIdAndDate(stone.getId(), requestDate).isPresent();
        if (exists) {
            throw new IllegalStateException("해당 날짜에 대한 기록이 이미 존재합니다.");
        }

        // powder 저장하는 로직
        int powderToAdd = 0;
        switch (request.getAchieveStatus()) {
            case A:
                powderToAdd = 10;
                break;
            case B:
                powderToAdd = 5;
                break;
            case C:
                break;
        }

        // Stone 엔티티의 powder 값을 업데이트
        stone.updatePowder(powderToAdd);

        Achieve achieve = Achieve.builder()
                .stone(stone)
                .achieveStatus(request.getAchieveStatus())
                .date(request.getDate())
                .build();

        stone.getAchieves().add(achieve); // 돌에 달성 기록 추가
        achieveRepository.save(achieve); // 달성 기록 저장

        return convertToResultDTO(achieve);
    }

    //달성률 계산
    public long calculateAchievementRate(UUID stoneId) {
        Stone stone = stoneRepository.findById(stoneId)
                .orElseThrow(() -> new RuntimeException("돌을 찾을 수 없습니다."));

        LocalDate startDate = stone.getStartDate().toLocalDate();
        LocalDate endDate = stone.getFinalDate().toLocalDate();
        LocalDate today = LocalDate.now();

        if (endDate.isBefore(today))
            endDate = today;

        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1; // 오늘 포함 계산

        // 시작일로부터 계산일까지 AchieveStatus.A 상태인 Achieve 개수
        long countAStatus = achieveRepository.findAllByStoneIdAndDateBetweenAndAchieveStatus(stoneId, startDate.atStartOfDay(), endDate.atTime(23, 59), AchieveStatus.A).size();

        // 달성률 계산
        return Math.round((double) countAStatus / totalDays * 100);
    }
}
