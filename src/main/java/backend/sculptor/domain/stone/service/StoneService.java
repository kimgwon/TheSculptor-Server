package backend.sculptor.domain.stone.service;

import backend.sculptor.domain.stone.dto.StoneCreateRequest;
import backend.sculptor.domain.stone.dto.StoneDetailDTO;
import backend.sculptor.domain.stone.dto.StoneListDTO;
import backend.sculptor.domain.stone.entity.Category;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.repository.StoneRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoneService {
    private final StoneRepository stoneRepository;

    //돌 전체 조회
    public List<StoneListDTO> getStonesByCategory(UUID userId, Category category) {
        List<Stone> stones;
        if (category == null) {
            stones = stoneRepository.findByUsersId(userId);
        } else {
            stones = stoneRepository.findByUsersIdAndCategory(userId, category);
        }
        return stones.stream().map(this::convertToStoneDTO).collect(Collectors.toList());
    }

    //돌 생성
    @Transactional
    public StoneListDTO createStone(StoneCreateRequest request){
        Stone stone = Stone.builder()
                .stoneName(request.getStoneName())
                .category(request.getCategory())
                .stoneGoal(request.getStoneGoal())
                .startDate(request.getStartDate())
                .build();
        return convertToStoneDTO(stoneRepository.save(stone));

    }

    //StoneList DTO 변환
    private StoneListDTO convertToStoneDTO(Stone stone) {
        // Stone 엔티티를 StoneDTO로 변환하는 로직
        String dDay = calculateDate(stone.getStartDate().toLocalDate());
        return new StoneListDTO(
                stone.getId(),
                stone.getStoneName(),
                stone.getCategory(),
                stone.getStoneGoal(),
                stone.getStartDate(),
                dDay
        );
    }

    //StoneDetail DTO 변환
    private StoneDetailDTO convertToDetailDTO(Stone stone){
        // Stone 엔티티를 DetailDTO로 변환하는 로직
        String dDay = calculateDate(stone.getStartDate().toLocalDate());
        //int achPoint = calculateAchieve()
        return new StoneDetailDTO(
                stone.getId(),
                stone.getStoneName(),
                stone.getCategory(),
                stone.getStoneGoal(),
                stone.getStartDate(),
                dDay,
                //달성률 추가
                //achPoint,
                stone.getPowder()
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

    //달성률 계산
    //public String calculateAchieve()


    //돌 하나 조회
    public StoneDetailDTO getStoneByStoneId(UUID userId, UUID stoneId){
        Optional<Stone> stoneOptional = stoneRepository.findByUsersIdAndId(userId, stoneId);
        return stoneOptional.map(this::convertToDetailDTO)
                .orElseThrow(() -> new EntityNotFoundException("해당 돌을 찾을 수 없습니다. ID: " + stoneId));
    }

}

