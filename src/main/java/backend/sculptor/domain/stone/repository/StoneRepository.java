package backend.sculptor.domain.stone.repository;

import backend.sculptor.domain.stone.entity.Category;
import backend.sculptor.domain.stone.entity.Stone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoneRepository extends JpaRepository<Stone, UUID> {
    List<Stone> findByUsersId(UUID userId);
    List<Stone> findByUsersIdAndCategory(UUID userId, Category category);

}