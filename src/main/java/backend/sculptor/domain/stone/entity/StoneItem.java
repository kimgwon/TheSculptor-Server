package backend.sculptor.domain.stone.entity;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class StoneItem extends StoneProduct{
    @Setter
    @Builder.Default
    private Boolean isWorn = false;

    @Builder
    public StoneItem(UUID id, Product product, Stone stone, Boolean isWorn) {
        super(id, product, stone);
        this.isWorn = isWorn;
    }
}
