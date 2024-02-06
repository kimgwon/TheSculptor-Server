package backend.sculptor.domain.follow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@IdClass(Follow.PK.class)
@NoArgsConstructor
@Getter
public class Follow {
    @Id
    @Column(name = "to_user", insertable = false, updatable = false)
    private UUID toUser;

    @Id
    @Column(name = "from_user", insertable = false, updatable = false)
    private UUID fromUser;

    @Builder
    public Follow(UUID toUser, UUID fromUser) {
        this.toUser = toUser;
        this.fromUser = fromUser;
    }

    public static class PK implements Serializable {
        UUID toUser;
        UUID fromUser;
    }
}
