package backend.sculptor.domain.stone.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Getter
@NoArgsConstructor
public class StoneDTO {

    private UUID stoneId;
    private String stoneName;
    private String category;
    private String stoneGoal;

    public StoneDTO(UUID stoneId, String stoneName, String category, String stoneGoal) {
        this.stoneId = stoneId;
        this.stoneName = stoneName;
        this.category = category;
        this.stoneGoal = stoneGoal;
    }


}

