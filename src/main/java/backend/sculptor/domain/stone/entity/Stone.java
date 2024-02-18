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

    @Enumerated(EnumType.STRING)
    private Category category; //enum

    private String stoneName;
    private String stoneGoal;
    private String oneComment;

    private int powder;
    public void updatePowder(int addPowder) { // user 엔티티 totalPowder 업데이트
        this.powder += addPowder;
        if (users != null) {
            users.updateTotalPowder(addPowder);
        }
    }

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime startDate;
    private LocalDateTime finalDate;

    @Setter
    private LocalDateTime lastManualChange;

    @Enumerated(EnumType.STRING)
    @Setter
    private StoneStatus status = StoneStatus.BASIC;

    @OneToMany(mappedBy = "stone", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Achieve> achieves = new ArrayList<>();

    @OneToMany(mappedBy = "stone", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "stone", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StoneLikes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "stone", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StoneType> stoneTypes = new ArrayList<>();

//    @OneToMany(mappedBy = "stone", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private List<StoneItem> stoneItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worn_type_id")
    private Type type;

    // Stone 착용 메서드
    public void wearType(Type type) {
        this.type = type;
    }

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
