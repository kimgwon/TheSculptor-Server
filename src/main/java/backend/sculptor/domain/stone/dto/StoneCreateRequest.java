package backend.sculptor.domain.stone.dto;

import backend.sculptor.domain.stone.entity.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonFormat
public class StoneCreateRequest {
    //private UUID stoneId;
    private String stoneName;
    private Category category;
    private String stoneGoal;
    private LocalDateTime startDate;
}
