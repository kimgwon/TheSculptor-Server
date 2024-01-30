package backend.sculptor.domain.stone.service;

import backend.sculptor.domain.stone.dto.StoneDTO;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.repository.StoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoneService {
    private final StoneRepository stoneRepository;

    //돌 전체 반환
    public List<StoneDTO> getStonesByCategory(String userId, String category) {
        List<Stone> stones;
        if (category == null || category.isEmpty()) {
            stones = stoneRepository.findByUserId(userId);
        } else {
            stones = stoneRepository.findByUserIdAndCategory(userId, category);
        }
        return stones.stream().map(this::convertToStoneDTO).collect(Collectors.toList());
    }

    //DTO 변환
    private StoneDTO convertToStoneDTO(Stone stone) {
        // Stone 엔티티를 StoneDTO로 변환하는 로직
        String categoryName = stone.getCategory().toString();
        return new StoneDTO(
                stone.getId(),
                stone.getStoneName(),
                categoryName,
                stone.getStoneGoal(),
                stone.getStartDate()
        );
    }

    //디데이 날짜 계산
    public String calculateDate(LocalDate startDate){
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(65);
        long daysBetween = ChronoUnit.DAYS.between(currentDate, endDate);
        if (daysBetween > 0) {
            return "D-" + daysBetween;
        } else if (daysBetween < 0) {
            return "D+" + Math.abs(daysBetween);
        } else {
            return "D-day";
        }
    }
}

