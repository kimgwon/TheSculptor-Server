package backend.sculptor.domain.comment.entity;

import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.user.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Comment {
    @Id @GeneratedValue
    @Column(name = "comment_id")
    private UUID id;

    private String content;
    private LocalDateTime writeAt;
    private int commentLike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="stone_id")
    private Stone stone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private Users users;
}
