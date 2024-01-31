package backend.sculptor.domain.user.repository;

import backend.sculptor.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users,Long> {
    public Optional<Users> findByName(String name);

    public Optional<Users> findById(UUID uuid);
}
