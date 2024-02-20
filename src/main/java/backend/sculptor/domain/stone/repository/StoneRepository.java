package backend.sculptor.domain.stone.repository;

import backend.sculptor.domain.stone.entity.Category;
import backend.sculptor.domain.stone.entity.Stone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoneRepository extends JpaRepository<Stone, UUID> {
    List<Stone> findByUsersId(UUID userId);
    List<Stone> findByUsersIdAndCategory(UUID userId, Category category);

    Optional<Stone> findByUsersIdAndId(UUID userId, UUID stoneId);

    List<Stone> findByUsersIdOrderByCreatedAtAsc(UUID userId);

    //돌 중복 검사
    Optional<Stone> findByUsersIdAndStoneNameAndCategoryAndStoneGoalAndStartDate(UUID userId, String stoneName, Category category, String stoneGoal, LocalDateTime startDate);

}