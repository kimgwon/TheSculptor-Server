package backend.sculptor.domain.museum.service;

import backend.sculptor.domain.comment.dto.CommentDTO;
import backend.sculptor.domain.comment.service.CommentService;
import backend.sculptor.domain.museum.dto.MuseumDetailDTO;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.service.AchieveService;
import backend.sculptor.domain.stone.service.StoneLikeService;
import backend.sculptor.domain.stone.service.StoneService;
import backend.sculptor.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MuseumDetailService {
    private final UserService userService;
    private final StoneService stoneService;
    private final StoneLikeService stoneLikeService;
    private final CommentService commentService;
    private final AchieveService achieveService;

    public MuseumDetailDTO getMuseumDetailInfo(UUID userId, UUID stoneId) {
        Stone stone = stoneService.getStoneByStoneIdAfterFinalDate(stoneId);
        List<CommentDTO.Info> comments = commentService.getComments(userId, stoneId);

        return MuseumDetailDTO.builder()
                .stone(convertToMuseumDetailStone(userId, stone))
                .comments(comments)
                .build();
    }

    private MuseumDetailDTO.Stone convertToMuseumDetailStone(UUID userId, Stone stone) {
        LocalDateTime startDate = stone.getStartDate();

        return MuseumDetailDTO.Stone.builder()
                .id(stone.getId())
                .name(stone.getStoneName())
                .category(stone.getCategory().name())
                .goal(stone.getStoneGoal())
                .startDate(startDate)
                .oneComment(stone.getOneComment())
                .isLike(stoneLikeService.isPressedLike(userId, stone.getId()))
                .isRepresent(userService.isRepresentStone(userId, stone))
                .dDay(stoneService.calculateDate(startDate.toLocalDate()))
                .powder(stone.getPowder())
                .achievementRate(achieveService.calculateAchievementRate(stone.getId()))
                .achievementCounts(achieveService.achievementCountsByStoneId(stone.getId()))
                .build();
    }
}