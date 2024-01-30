package backend.sculptor.domain.stone.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@NoArgsConstructor
public class StoneDTO {

    private UUID stoneId;
    private String stoneName;
    private String category;
    private String stoneGoal;
    private LocalDateTime startDate;

    public StoneDTO(UUID stoneId, String stoneName, String category, String stoneGoal, LocalDateTime startDate) {
        this.stoneId = stoneId;
        this.stoneName = stoneName;
        this.category = category;
        this.stoneGoal = stoneGoal;
        this.startDate = startDate;
    }


}

