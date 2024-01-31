package backend.sculptor.domain.stone.service;

import backend.sculptor.domain.stone.dto.StoneListDTO;
import backend.sculptor.domain.stone.entity.Category;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.repository.StoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoneService {
    private final StoneRepository stoneRepository;

    //돌 전체 반환
    public List<StoneListDTO> getStonesByCategory(UUID userId, Category category) {
        List<Stone> stones;
        if (category == null) {
            stones = stoneRepository.findByUsersId(userId);
        } else {
            stones = stoneRepository.findByUsersIdAndCategory(userId, category);
        }
        return stones.stream().map(this::convertToStoneDTO).collect(Collectors.toList());
    }

    //DTO 변환
    private StoneListDTO convertToStoneDTO(Stone stone) {
        // Stone 엔티티를 StoneDTO로 변환하는 로직
        String categoryName = stone.getCategory().toString();
        String dDay = calculateDate(stone.getStartDate().toLocalDate());
        return new StoneListDTO(
                stone.getId(),
                stone.getStoneName(),
                categoryName,
                stone.getStoneGoal(),
                stone.getStartDate(),
                dDay
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

