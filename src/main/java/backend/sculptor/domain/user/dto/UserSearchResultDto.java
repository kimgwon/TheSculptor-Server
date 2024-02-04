package backend.sculptor.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserSearchResultDto {
    private UUID id;
    private String username;
    private String profileUrl;
}
