package backend.sculptor.domain.museum.service;

import backend.sculptor.domain.comment.entity.Comment;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.repository.StoneRepository;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.domain.museum.dto.MuseumDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MuseumDetailService {

//    private final UserRepository userRepository;
//    private final StoneRepository stoneRepository;
//    private final CommentRepository commentRepository;
//
//    @Autowired
//    public MuseumDetailService(UserRepository userRepository, StoneRepository stoneRepository, CommentRepository commentRepository) {
//        this.userRepository = userRepository;
//        this.stoneRepository = stoneRepository;
//        this.commentRepository = commentRepository;
//    }
//
//    public Optional<MuseumDetail> getMuseumDetailInfo(UUID ownerId, UUID stoneId) {
//        Optional<Stone> optionalStone = stoneRepository.findById(stoneId);
//        List<Comment> comments = commentRepository.findByStoneId(stoneId);
//
//        return optionalStone.map(stone -> convertToMuseumDetail(stone, comments));
//    }
//
//    private MuseumDetail convertToMuseumDetail(Stone stone, List<Comment> comments) {
//        MuseumDetail museumDetail = new MuseumDetail();
//
//        museumDetail.setStone(convertToMuseumDetailStone(stone));
//        museumDetail.setComments(convertToMuseumDetailComments(comments));
//
//        return museumDetail;
//    }
//
//    private MuseumDetail.Stone convertToMuseumDetailStone(Stone stone) {
//        MuseumDetail.Stone museumDatailStone = new MuseumDetail.Stone();
//        museumDatailStone.setId(stone.getId());
//        museumDatailStone.setName(stone.getStoneName());
//        museumDatailStone.setCategory(stone.getCategory().name());
//        museumDatailStone.setGoal(stone.getStoneGoal());
//        museumDatailStone.setStartDate(stone.getStartDate());
//        museumDatailStone.setOneComment(stone.getOneComment());
//        museumDatailStone.setDDay(stone.getFinalDate());
//        museumDatailStone.setPowder(stone.getPowder());
//
//        return museumDatailStone;
//    }
//
//    private List<MuseumDetail.Comment> convertToMuseumDetailComments(List<Comment> comments) {
//        return comments.stream()
//                .map(comment -> {
//                    MuseumDetail.Comment commentDTO = new MuseumDetail.Comment();
//                    Users writer = userRepository.findById(comment.getUserId());
//
//                    commentDTO.setGuestId(comment.getUserId());
//                    commentDTO.setGuestNickname(writer.getNickname());
//                    commentDTO.setContent(comment.getContent());
//                    commentDTO.setLike(comment.isLike());
//                    commentDTO.setDate(comment.getWriteAt());
//
//                    return commentDTO;
//                })
//                .collect(Collectors.toList());
//    }
}
