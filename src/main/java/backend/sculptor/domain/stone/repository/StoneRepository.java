package backend.sculptor.domain.stone.repository;

import backend.sculptor.domain.stone.entity.Stone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoneRepository extends JpaRepository<Stone, UUID> {
    @Query("SELECT s FROM Stone s WHERE s.users.id = :userId")
    List<Stone> findByUserId(@Param("userId") String userId);
    //List<Stone> findByUserIdAndCategory(String userId, String category);

    @Query("SELECT s FROM Stone s JOIN s.users u WHERE u.id = :userId AND s.category = :category")
    List<Stone> findByUserIdAndCategory(@Param("userId") String userId, @Param("category") String category);

}