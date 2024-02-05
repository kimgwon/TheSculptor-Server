package backend.sculptor.domain.stone.entity;

import backend.sculptor.domain.user.entity.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class StoneLikes {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "stone_id")
    private UUID stoneId;

    @Builder
    public StoneLikes(UUID stoneId, UUID userId) {
        this.stoneId = stoneId;
        this.userId = userId;
    }
}
