package backend.sculptor.domain.follow.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class FollowSimpleListDto {
    private UUID id;
    private String nickname;
    private UUID representStoneId;
}
