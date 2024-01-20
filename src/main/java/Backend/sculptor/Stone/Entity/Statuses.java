package Backend.sculptor.Stone.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Statuses {
    @Id @GeneratedValue
    @Column(name = "status_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stone_id")
    private Stones stones;

    @Enumerated(EnumType.STRING)
    private StoneStatus stone_status; //enum

    private LocalDateTime updated_at;

}
