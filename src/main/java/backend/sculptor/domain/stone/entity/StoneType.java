package backend.sculptor.domain.stone.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class StoneType extends StoneProduct{
    @Builder
    public StoneType(UUID id, Product product, Stone stone) {
        super(id, product, stone);
    }
}