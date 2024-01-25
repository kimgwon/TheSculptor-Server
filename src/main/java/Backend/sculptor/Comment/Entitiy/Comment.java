package Backend.sculptor.Comment.Entitiy;

import Backend.sculptor.Stone.Entity.Stone;
import Backend.sculptor.User.Entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
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
    private LocalDateTime write_at;
    private int comment_like;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="stone_id")
    private Stone stone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private Users users;
}
