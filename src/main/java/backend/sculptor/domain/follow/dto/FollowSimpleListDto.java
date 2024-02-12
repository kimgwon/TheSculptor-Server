package backend.sculptor.domain.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class FollowSimpleListDto {
    private UUID id;
    private String nickname;
    private String profileImage;
    private UUID representStoneId;
}
