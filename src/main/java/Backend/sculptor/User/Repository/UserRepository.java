package Backend.sculptor.User.Repository;

import Backend.sculptor.User.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users,Long> {
    public Optional<Users> findByName(String name);

    public Optional<Users> findById(UUID uuid);
}
