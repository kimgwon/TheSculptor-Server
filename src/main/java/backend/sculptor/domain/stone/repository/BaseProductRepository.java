package backend.sculptor.domain.stone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BaseProductRepository<T> extends JpaRepository<T, UUID> {
    Optional<T> findById(UUID id);
}