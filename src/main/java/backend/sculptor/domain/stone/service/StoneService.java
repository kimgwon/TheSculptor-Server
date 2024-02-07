package backend.sculptor.domain.stone.service;

import backend.sculptor.domain.stone.dto.StoneCreateRequest;
import backend.sculptor.domain.stone.dto.StoneDetailDTO;
import backend.sculptor.domain.stone.dto.StoneListDTO;
import backend.sculptor.domain.stone.entity.Category;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.repository.StoneRepository;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.global.exception.BadRequestException;
import backend.sculptor.global.exception.ErrorCode;
import backend.sculptor.global.exception.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final UserRepository userRepository;
    private final AchieveService achieveService;

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

    // 목표일 이후 날짜의 돌 전체 조회 (박물관)
    public List<StoneListDTO> getStonesByUserIdAfterFinalDate(UUID userId) {
        List<Stone> stones = stoneRepository.findByUsersId(userId);
        LocalDateTime currentDate = LocalDateTime.now();
        return stones.stream()
                .filter(stone -> stone.getFinalDate().isBefore(currentDate)) // 목표일 이후인 돌만 필터링
                .map(this::convertToStoneDTO)
                .toList();
    }

    //돌 생성
    @Transactional
    public StoneListDTO createStone(UUID userId,StoneCreateRequest request){
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 중복 돌 검사
        Optional<Stone> existingStone = stoneRepository.findByUsersIdAndStoneNameAndCategoryAndStoneGoalAndStartDate(
                userId, request.getStoneName(), request.getCategory(), request.getStoneGoal(), request.getStartDate());
        if (existingStone.isPresent()) {
            // 중복 돌이 존재하면 예외 발생 또는 다른 적절한 처리
            throw new IllegalStateException("이미 동일한 정보의 돌이 존재합니다.");
        }

        Stone stone = Stone.builder()
                .users(user)
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
                stone.getUsers().getId(),
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
        long achRate = achieveService.calculateAchievementRate(stone.getId());
        return new StoneDetailDTO(
                stone.getId(),
                stone.getStoneName(),
                stone.getCategory(),
                stone.getStoneGoal(),
                stone.getStartDate(),
                dDay,
                achRate,
                stone.getPowder(),
                stone.getStoneLike()
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

    //돌 하나 조회
    public StoneDetailDTO getStoneByStoneId(UUID userId, UUID stoneId){
        Optional<Stone> stoneOptional = stoneRepository.findByUsersIdAndId(userId, stoneId);
        return stoneOptional.map(this::convertToDetailDTO)
                .orElseThrow(() -> new EntityNotFoundException("해당 돌을 찾을 수 없습니다. ID: " + stoneId));
    }


    // 목표일 이후 날짜의 돌 하나 조회 (박물관)
    public Stone getStoneByStoneIdAfterFinalDate(UUID stoneId) {
        Stone stone = stoneRepository.findById(stoneId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STONE_NOT_FOUND.getMessage()));

        // 돌의 목표일이 오늘 날짜 이후인지 확인
        if (stone.getFinalDate().isAfter(LocalDateTime.now())) {
            throw new BadRequestException(ErrorCode.STONE_NOT_COMPLETE.getMessage());
        }

        return stone;
    }
}

