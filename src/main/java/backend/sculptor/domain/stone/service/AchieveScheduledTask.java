package backend.sculptor.domain.stone.service;

import backend.sculptor.domain.stone.entity.Achieve;
import backend.sculptor.domain.stone.entity.AchieveStatus;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.repository.AchieveRepository;
import backend.sculptor.domain.stone.repository.StoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AchieveScheduledTask {

    private final AchieveRepository achieveRepository;
    private final StoneRepository stoneRepository;


    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void recordMissingAchieves() {
        LocalDate yesterday = LocalDate.now().minusDays(1); // 어제 날짜
        List<Stone> stones = stoneRepository.findAll(); // 모든 Stone 조회

        stones.forEach(stone -> {
            LocalDate startDate = stone.getStartDate().toLocalDate();
            LocalDate finalDate = stone.getFinalDate().toLocalDate();

            // 어제 날짜가 시작일과 종료일 사이인지 확인
            if (!yesterday.isBefore(startDate) && !yesterday.isAfter(finalDate)) {
                boolean exists = achieveRepository.findByStoneIdAndDate(stone.getId(), yesterday.atStartOfDay()).isPresent();
                if (!exists) {
                    // 어제 날짜에 대한 Achieve 기록이 없으면 C 상태로 기록 추가
                    Achieve achieve = Achieve.builder()
                            .stone(stone)
                            .achieveStatus(AchieveStatus.C)
                            .date(LocalDateTime.of(yesterday, LocalTime.MIDNIGHT))
                            .build();
                    achieveRepository.save(achieve);
                }
            }
        });
    }


}

