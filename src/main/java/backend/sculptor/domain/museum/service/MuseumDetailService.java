package backend.sculptor.domain.museum.service;

import backend.sculptor.domain.comment.dto.CommentDTO;
import backend.sculptor.domain.comment.service.CommentService;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.repository.StoneRepository;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.domain.museum.dto.MuseumDetailDTO;
import backend.sculptor.global.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MuseumDetailService {

    private final UserRepository userRepository;
    private final StoneRepository stoneRepository;
    private final CommentService commentService;

    @Autowired
    public MuseumDetailService(UserRepository userRepository, StoneRepository stoneRepository, CommentService commentService) {
        this.userRepository = userRepository;
        this.stoneRepository = stoneRepository;
        this.commentService = commentService;
    }

    public MuseumDetailDTO getMuseumDetailInfo(UUID userId, UUID ownerId, UUID stoneId) {
        MuseumDetailDTO museumDetailDTO = new MuseumDetailDTO();

        userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("User not found"));
        Stone stone = stoneRepository.findById(stoneId)
                .orElseThrow(() -> new NotFoundException("Stone not found"));
        List<CommentDTO.Info> comments = commentService.getComments(userId, ownerId, stoneId);

        museumDetailDTO.setStone(convertToMuseumDetailStone(stone));
        museumDetailDTO.setComments(comments);

        return museumDetailDTO;
    }

    private MuseumDetailDTO.Stone convertToMuseumDetailStone(Stone stone) {
        MuseumDetailDTO.Stone museumDatailStone = new MuseumDetailDTO.Stone();

        museumDatailStone.setId(stone.getId());
        museumDatailStone.setName(stone.getStoneName());
        museumDatailStone.setCategory(stone.getCategory().name());
        museumDatailStone.setGoal(stone.getStoneGoal());
        museumDatailStone.setStartDate(stone.getStartDate());
        museumDatailStone.setOneComment(stone.getOneComment());
        museumDatailStone.setDDay(stone.getFinalDate());
        museumDatailStone.setPowder(stone.getPowder());

        return museumDatailStone;
    }
}