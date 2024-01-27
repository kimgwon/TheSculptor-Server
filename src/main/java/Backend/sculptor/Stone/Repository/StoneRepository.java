package Backend.sculptor.Stone.Repository;

import Backend.sculptor.Stone.Entity.Stone;
import Backend.sculptor.User.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoneRepository extends JpaRepository<Stone, Long> {
    List<Stone> findByUserId(String userId);
    List<Stone> findByUserIdAndCategory(String userId, String category);

}