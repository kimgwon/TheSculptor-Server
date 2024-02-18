package backend.sculptor.domain.stone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaseStoneProductRepository<T> extends JpaRepository<T, UUID> {
    List<T> findAllByStoneId(UUID stoneId);
    Optional<T> findByStoneIdAndProductId(UUID stoneId, UUID productId);
}