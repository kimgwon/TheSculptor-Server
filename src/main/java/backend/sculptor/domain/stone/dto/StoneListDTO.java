package backend.sculptor.domain.stone.dto;

import backend.sculptor.domain.stone.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoneListDTO {

    private UUID userId;
    private UUID stoneId;
    private String stoneName;
    private Category category;
    private String stoneGoal;
    private LocalDateTime startDate;
    private String dDay;

}