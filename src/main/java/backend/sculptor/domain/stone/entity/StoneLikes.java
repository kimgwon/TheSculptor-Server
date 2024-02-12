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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stone_id")
    private Stone stone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Builder
    public StoneLikes(Stone stone, Users user) {
        this.stone = stone;
        this.user = user;
    }
}
