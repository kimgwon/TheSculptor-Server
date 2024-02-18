package backend.sculptor.domain.stone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public abstract class StoneProduct {
    @Id @GeneratedValue
    @Column(name = "stone_product_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="stone_id")
    private Stone stone;
}
