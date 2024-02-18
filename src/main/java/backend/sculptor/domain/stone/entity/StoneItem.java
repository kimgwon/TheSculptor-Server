package backend.sculptor.domain.stone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class StoneItem {
    @Id @GeneratedValue
    @Column(name = "stone_item_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="stone_id")
    private Stone stone;
}
