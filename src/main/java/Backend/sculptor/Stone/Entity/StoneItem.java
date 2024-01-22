package Backend.sculptor.Stone.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class StoneItem {
    @Id @GeneratedValue
    @Column(name = "stone_item_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="stone_id")
    private Stone stone;

}
