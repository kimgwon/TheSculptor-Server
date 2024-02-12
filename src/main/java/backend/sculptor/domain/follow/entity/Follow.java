package backend.sculptor.domain.follow.entity;

import backend.sculptor.domain.user.entity.Users;
import jakarta.persistence.*;
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
    @ManyToOne
    @JoinColumn(name = "to_user", referencedColumnName = "id", insertable = false, updatable = false)
    private Users toUser;

    @Id
    @ManyToOne
    @JoinColumn(name = "from_user", referencedColumnName = "id", insertable = false, updatable = false)
    private Users fromUser;

    @Builder
    public Follow(Users toUser, Users fromUser) {
        this.toUser = toUser;
        this.fromUser = fromUser;
    }

    public static class PK implements Serializable {
        UUID toUser;
        UUID fromUser;
    }
}
