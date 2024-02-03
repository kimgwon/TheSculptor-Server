package backend.sculptor.domain.stone.dto;

import backend.sculptor.domain.stone.entity.AchieveStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonFormat
public class StoneSculptRequest {

    private AchieveStatus achieveStatus;
    private LocalDateTime date;

    // 유효성 검사 메서드
    public boolean isValid() {
        return achieveStatus != null && date != null;

    }
}
