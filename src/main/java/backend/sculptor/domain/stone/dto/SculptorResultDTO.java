package backend.sculptor.domain.stone.dto;

import backend.sculptor.domain.stone.entity.AchieveStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SculptorResultDTO {

    private UUID achieveId;
    private UUID stoneId;
    private LocalDate date;
    private AchieveStatus achieveStatus;
    private int powder;
}
