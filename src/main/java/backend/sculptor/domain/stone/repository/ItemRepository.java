package backend.sculptor.domain.stone.repository;

import backend.sculptor.domain.stone.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {
    Optional<Item> findById(UUID itemId);
    List<Item> findAll();
}
