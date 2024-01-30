package backend.sculptor.domain.stone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private UUID id;

    private String item_name;
    private int item_price;
}
