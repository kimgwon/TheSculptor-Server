package backend.sculptor.domain.stone.repository;

import backend.sculptor.domain.stone.entity.Achieve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AchieveRepository extends JpaRepository<Achieve, UUID> {
    //날짜 중복 검사
    Optional<Achieve> findByStoneIdAndDate(UUID stoneId, LocalDateTime date);
}
