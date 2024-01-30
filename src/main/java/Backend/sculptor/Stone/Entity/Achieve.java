package Backend.sculptor.Stone.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Achieve {
    @Id
    @GeneratedValue
    @Column(name = "achieve_id")
    private UUID id;

    @OneToOne(mappedBy = "achieve")
    private Stone stone;

    @Enumerated(EnumType.STRING)
    private AchieveStatus achieveStatus;

    private LocalDateTime date;



}
