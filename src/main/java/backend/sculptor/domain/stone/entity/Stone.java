package backend.sculptor.domain.stone.entity;

import backend.sculptor.domain.comment.entity.Comment;
import backend.sculptor.domain.user.entity.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Stone {

    @Id @GeneratedValue
    @Column(name = "stone_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    private String stoneName;
    private String stoneGoal;
    private String oneComment;

    @Enumerated(EnumType.STRING)
    private Category category; //enum

    @Setter
    private int powder;
    public void updatePowder(int addPowder) {
        this.powder += addPowder;
        if (users != null) {
            users.updateTotalPowder(addPowder); // User 엔티티에 있는 totalPowder를 업데이트하는 메서드 호출
        }
    }

    private LocalDateTime startDate;
    private LocalDateTime finalDate;
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "stone", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StoneLikes> likes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Setter
    private StoneStatus status = StoneStatus.BASIC;

    @OneToMany(mappedBy = "stone", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StoneItem> stoneTypes = new ArrayList<>();

    @OneToMany(mappedBy = "stone", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "stone", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Achieve> achieves = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worn_type_id")
    private Item type;

    // 원석 착용 메서드
    public void wearType(Item type) {
        this.type = type;
    }

    @Setter
    private LocalDateTime lastManualChange;

    /*
    //갱신일
    private LocalDateTime updated_at;
     */

    @Builder
    public Stone(Users users,String stoneName, Category category, String stoneGoal, LocalDateTime startDate){
        this.users = users;
        this.stoneName = stoneName;
        this.category = category;
        this.stoneGoal = stoneGoal;
        this.startDate = startDate;
        this.finalDate = startDate.plusDays(65);
    }

}
