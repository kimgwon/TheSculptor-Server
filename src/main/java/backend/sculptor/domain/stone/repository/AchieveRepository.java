package backend.sculptor.domain.stone.repository;

import backend.sculptor.domain.stone.entity.Achieve;
import backend.sculptor.domain.stone.entity.AchieveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AchieveRepository extends JpaRepository<Achieve, UUID> {
    //날짜 중복 검사
    Optional<Achieve> findByStoneIdAndDate(UUID stoneId, LocalDateTime date);

    //달성률 계산
    List<Achieve> findAllByStoneIdAndDateBetweenAndAchieveStatus(UUID stoneId, LocalDateTime startDate, LocalDateTime endDate, AchieveStatus achieveStatus);

    List<Achieve> findByStoneId(UUID stoneId);

    //StoneID를 기준으로 모든 Achieve 기록을 날짜 내림차순으로 조회
    List<Achieve> findByStoneIdOrderByDateDesc(UUID stoneId);
}
