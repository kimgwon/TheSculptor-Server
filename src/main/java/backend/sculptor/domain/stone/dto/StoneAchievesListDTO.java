package backend.sculptor.domain.stone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoneAchievesListDTO {

    private UUID stoneId;
    private List<AchieveDTO> achieves;
}
