package backend.sculptor.domain.stone.service;

import backend.sculptor.domain.home.dto.UserRepresentStone;
import backend.sculptor.domain.stone.dto.StoneCreateRequest;
import backend.sculptor.domain.stone.dto.StoneDetailDTO;
import backend.sculptor.domain.stone.dto.StoneListDTO;
import backend.sculptor.domain.stone.entity.*;
import backend.sculptor.domain.stone.repository.AchieveRepository;
import backend.sculptor.domain.stone.repository.StoneItemRepository;
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
    private final AchieveRepository achieveRepository;
    private final AchieveService achieveService;
    private final StoneItemRepository stoneItemRepository;

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
    public List<Stone> getStonesByUserIdAfterFinalDate(UUID userId) {
        List<Stone> stones = stoneRepository.findByUsersId(userId);
        LocalDateTime currentDate = LocalDateTime.now();
        return stones.stream()
                .filter(stone -> stone.getFinalDate().isBefore(currentDate)) // 목표일 이후인 돌만 필터링
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

    @Transactional
    public void deleteStone(UUID userId, UUID stoneId) {
        Stone stone = getStoneByUserIdAndStoneId(userId, stoneId);
        Users user = userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        if (stone.equals(user.getRepresentStone()))
            user.setRepresentStone(null);

        delete(stone);
    }

    // 돌 삭제
    @Transactional
    public void delete(Stone stone){
        stoneRepository.delete(stone);
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
                stone.getStatus(),
                stone.getLikes().size()
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
    @Transactional
    public StoneDetailDTO getStoneByStoneId(UUID userId, UUID stoneId){
        updateStoneStatusBasedOnAchieves(stoneId);
        Optional<Stone> stoneOptional = stoneRepository.findByUsersIdAndId(userId, stoneId);
        return stoneOptional.map(this::convertToDetailDTO)
                .orElseThrow(() -> new EntityNotFoundException("해당 돌을 찾을 수 없습니다. ID: " + stoneId));
    }

    public Stone getStoneByUserIdAndStoneId(UUID userId, UUID stoneId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        Stone stone = stoneRepository.findById(stoneId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STONE_NOT_FOUND.getMessage()));

        if (!userId.equals(stone.getUsers().getId())) {
            throw new BadRequestException(ErrorCode.NOT_USER_STONE.getMessage());
        }

        return stone;
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

    public Stone getStoneFirstCreated(UUID userId) {
        List<Stone> stones = stoneRepository.findByUsersIdOrderByCreatedAtAsc(userId);

        if (stones.isEmpty()) {
            return null;
        } else {
            return stones.get(0);
        }
    }

    //돌 상태 변화 감지 로직
    @Transactional
    public void updateStoneStatusBasedOnAchieves(UUID stoneId) {

        Stone stone = stoneRepository.findById(stoneId)
                .orElseThrow(() -> new IllegalArgumentException("해당 돌을 찾을 수 없습니다. ID: "+ stoneId));

        LocalDateTime lastManualChange = stone.getLastManualChange();

        List<Achieve> recentAchieves;
        if (lastManualChange == null) {
            // lastManualChange가 null일 경우, 모든 Achieve 기록을 조회
            recentAchieves = achieveRepository.findByStoneIdOrderByDateAsc(stoneId);
        } else {
            // lastManualChange 이후의 Achieve 기록을 조회
            recentAchieves = achieveRepository.findByStoneIdAndDateAfterOrderByDateAsc(stoneId, lastManualChange);
        }
        //List<Achieve> recentAchieves = achieveRepository.findByStoneIdAndDateAfter(stoneId, lastManualChange);

        //List<Achieve> achieves = achieveRepository.findByStoneIdOrderByDateDesc(stoneId);
        int consecutiveCs = 0;

        StoneStatus stoneStatus = null;
        for (Achieve achieve : recentAchieves) {
            if (achieve.getAchieveStatus() == AchieveStatus.C) {
                consecutiveCs++;
                stoneStatus = determineStoneStatus(consecutiveCs);
                if (stoneStatus != null) {
                    // 조건을 만족하는 경우, 즉시 루프 종료
                    break;
                }
            } else {
                consecutiveCs = 0; // 연속성이 깨질 경우 카운트 리셋
            }
        }


        StoneStatus newStatus = determineStoneStatus(consecutiveCs); // 상태 결정 로직 호출
        if (newStatus != null) {
            stone.setStatus(newStatus); // 상태 업데이트
            stoneRepository.save(stone); // 변경 사항 저장
        }
    }

    private StoneStatus determineStoneStatus(int consecutiveCs) {
        if (consecutiveCs >= 12) {
            return StoneStatus.BROKEN;
        } else if (consecutiveCs >= 10) {
            return StoneStatus.L_CRACK;
        } else if (consecutiveCs >= 7) {
            return StoneStatus.S_CRACK;
        } else if (consecutiveCs >= 5) {
            return StoneStatus.PLANT;
        } else if (consecutiveCs >= 3) {
            return StoneStatus.MOSS;
        } else {
            return null; // 아직 조건을 만족하는 상태가 아님
        }
    }

    //이끼 제거
    @Transactional
    public void removeMoss(UUID stoneId) {
        Stone stone = stoneRepository.findById(stoneId)
                .orElseThrow(() -> new IllegalArgumentException("해당 돌을 찾을 수 없습니다. ID: " + stoneId));

        final int mossRemovalCost = 50;

        if (!(stone.getStatus() == StoneStatus.MOSS || stone.getStatus() == StoneStatus.PLANT)) {
            // 돌 상태가 적합하지 않은 경우
            throw new IllegalStateException("돌 상태가 이끼(MOSS) 또는 식물(PLANT)이 아니어서 이끼제거를 수행할 수 없습니다.");
        } else if (stone.getPowder() < mossRemovalCost) {
            // 필요 포인트가 부족한 경우
            throw new IllegalStateException("이끼제거를 수행하기에 포인트가 부족합니다. 필요 포인트: " + mossRemovalCost + ", 현재 포인트: " + stone.getPowder());
        }

        // 조건을 만족하는 경우, 돌 상태 업데이트 및 포인트 차감
        stone.setStatus(StoneStatus.BASIC);
        stone.updatePowder(-mossRemovalCost);

        stone.setLastManualChange(LocalDateTime.now());
        stoneRepository.save(stone);
    }


    //균열 메꾸기
    @Transactional
    public void repairCrack(UUID stoneId) {
        Stone stone = stoneRepository.findById(stoneId)
                .orElseThrow(() -> new IllegalArgumentException("해당 돌을 찾을 수 없습니다. ID: " + stoneId));

        final int crackRepairCost = 100; // 균열 메꾸기에 필요한 포인트

        if (!(stone.getStatus() == StoneStatus.S_CRACK || stone.getStatus() == StoneStatus.L_CRACK)) {
            throw new IllegalStateException("돌 상태가 실금(S_CRACK) 또는 균열(L_CRACK)이 아니어서 균열 메꾸기를 수행할 수 없습니다.");
        } else if (stone.getPowder() < crackRepairCost) {
            throw new IllegalStateException("균열 메꾸기를 수행하기에 포인트가 부족합니다. 필요 포인트: " + crackRepairCost + ", 현재 포인트: " + stone.getPowder());
        }

        stone.setStatus(StoneStatus.BASIC);
        stone.updatePowder(-crackRepairCost);
        stone.setLastManualChange(LocalDateTime.now()); // 마지막 수동 변경 시간 기록
        stoneRepository.save(stone);
    }


    public List<StoneItem> findAllStoneItem(StoneListDTO stone) {
        List<StoneItem> result = stoneItemRepository.findAllByStone(stone);
        return result;
    }

    public UserRepresentStone.Stone convertToUserRepresenstStone(Stone stone) {
        return UserRepresentStone.Stone.builder()
                .id(stone.getId())
                .name(stone.getStoneName())
                .goal(stone.getStoneGoal())
                .startDate(stone.getStartDate())
                .dDay(calculateDate(stone.getStartDate().toLocalDate()))
                .achievementRate(achieveService.calculateAchievementRate(stone.getId()))
                .build();
    }
}


