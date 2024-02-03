package backend.sculptor.domain.stone.dto;

import backend.sculptor.domain.stone.entity.AchieveStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AchieveDTO {
    private UUID achieveId;
    private LocalDateTime date;
    private AchieveStatus achieveStatus;

}

