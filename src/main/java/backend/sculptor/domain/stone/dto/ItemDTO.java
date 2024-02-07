package backend.sculptor.domain.stone.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ItemDTO {
    private UUID id;
    private String itemName;
    private int itemPrice;

    public ItemDTO(UUID id, String itemName, int itemPrice) {
        this.id = id;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }
}
