package Backend.sculptor.Member.Repository;

import Backend.sculptor.Member.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Long> {
    public Optional<Users> findByName(String name);
}
