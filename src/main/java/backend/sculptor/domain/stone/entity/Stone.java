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

    private LocalDateTime startDate;
    private LocalDateTime finalDate;
    @CreationTimestamp
    private LocalDateTime createdAt;

    private int stoneLike;

//    @OneToMany(mappedBy = "stones")
//    private List<Status> statuses;

    @Enumerated(EnumType.STRING)
    @Setter
    private StoneStatus status = StoneStatus.BASIC;

    @OneToMany(mappedBy = "stone")
    private List<StoneItem> stoneItems = new ArrayList<>();

    @OneToMany(mappedBy = "stone")
    private List<Comment> comments = new ArrayList<>();

//    @OneToOne
//    @JoinColumn(name="achieve_id")
//    private Achieve achieve;

    @OneToMany(mappedBy = "stone", cascade = CascadeType.ALL)
    private List<Achieve> achieves = new ArrayList<>();

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
