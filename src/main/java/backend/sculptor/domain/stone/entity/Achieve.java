package backend.sculptor.domain.stone.entity;

import backend.sculptor.domain.user.entity.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

//    @OneToOne(mappedBy = "achieve")
//    private Stone stone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stone_id")
    private Stone stone;

    @Enumerated(EnumType.STRING)
    private AchieveStatus achieveStatus;

    private LocalDate date;

    @Builder
    public Achieve(Stone stone,AchieveStatus achieveStatus, LocalDate date){
        this.stone = stone;
        this.achieveStatus = achieveStatus;
        this.date = date;
    }



}
