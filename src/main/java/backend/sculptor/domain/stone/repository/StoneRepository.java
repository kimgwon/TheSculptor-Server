package backend.sculptor.domain.stone.repository;

import backend.sculptor.domain.stone.entity.Stone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoneRepository extends JpaRepository<Stone, Long> {
    List<Stone> findByUserId(String userId);
    List<Stone> findByUserIdAndCategory(String userId, String category);

}