package Backend.sculptor.Stone.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Getter
@NoArgsConstructor
public class StoneDTO {

    private UUID stone_id;
    private String stone_name;
    private String category;
    private String stone_goal;

    public StoneDTO(UUID stone_id, String stone_name, String category, String stone_goal) {
        this.stone_id = stone_id;
        this.stone_name = stone_name;
        this.category = category;
        this.stone_goal = stone_goal;
    }


}

