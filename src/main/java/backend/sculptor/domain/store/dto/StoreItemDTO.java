package backend.sculptor.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class StoreItemDTO {
    private UUID itemId;
    private String itemName;
    private int itemPrice;
}
