package backend.sculptor.domain.museum.service;

import backend.sculptor.domain.comment.dto.CommentDTO;
import backend.sculptor.domain.comment.service.CommentService;
import backend.sculptor.domain.museum.dto.MuseumDetailDTO;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.service.AchieveService;
import backend.sculptor.domain.stone.service.StoneService;
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
    private final StoneService stoneService;
    private final CommentService commentService;
    private final AchieveService achieveService;

    public MuseumDetailDTO getMuseumDetailInfo(UUID userId, UUID stoneId) {
        MuseumDetailDTO museumDetailDTO = new MuseumDetailDTO();

        Stone stone = stoneService.getStoneByStoneIdAfterFinalDate(stoneId);
        List<CommentDTO.Info> comments = commentService.getComments(userId, stoneId);

        museumDetailDTO.setStone(convertToMuseumDetailStone(stone));
        museumDetailDTO.setComments(comments);

        return museumDetailDTO;
    }

    private MuseumDetailDTO.Stone convertToMuseumDetailStone(Stone stone) {
        MuseumDetailDTO.Stone museumDatailStone = new MuseumDetailDTO.Stone();
        LocalDateTime startDate = stone.getStartDate();

        museumDatailStone.setId(stone.getId());
        museumDatailStone.setName(stone.getStoneName());
        museumDatailStone.setCategory(stone.getCategory().name());
        museumDatailStone.setGoal(stone.getStoneGoal());
        museumDatailStone.setStartDate(startDate);
        museumDatailStone.setOneComment(stone.getOneComment());
        museumDatailStone.setDDay(stoneService.calculateDate(startDate.toLocalDate()));
        museumDatailStone.setPowder(stone.getPowder());

        return museumDatailStone;
    }
}