package backend.sculptor.domain.stone.repository;

import backend.sculptor.domain.stone.entity.StoneLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoneLikesRepository extends JpaRepository<StoneLikes, UUID> {
    Optional<StoneLikes> findByUserIdAndStoneId(UUID userId, UUID stoneId);
}
