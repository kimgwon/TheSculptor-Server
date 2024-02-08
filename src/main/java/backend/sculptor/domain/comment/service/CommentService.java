package backend.sculptor.domain.comment.service;

import backend.sculptor.domain.comment.dto.CommentDTO;
import backend.sculptor.domain.comment.dto.CommentLikeDTO;
import backend.sculptor.domain.comment.entity.Comment;
import backend.sculptor.domain.comment.entity.CommentLike;
import backend.sculptor.domain.comment.repository.CommentRepository;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.service.StoneService;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.global.exception.ErrorCode;
import backend.sculptor.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final StoneService stoneService;
    private final CommentRepository commentRepository;
    private final CommentLikeService commentLikeService;

    public List<CommentDTO.Info> getComments(UUID userId, UUID stoneId) {
        stoneService.getStoneByStoneIdAfterFinalDate(stoneId);
        List<Comment> comments = getCommentsByStoneId(stoneId);

        return convertToCommentResponse(userId, comments);
    }

    private List<CommentDTO.Info> convertToCommentResponse(UUID userId, List<Comment> comments) {
        return comments.stream()
                .map(originalComment -> {
                    Users writer = originalComment.getWriter();

                    return CommentDTO.Info.builder()
                            .id(originalComment.getId())
                            .writerId(writer.getId())
                            .writerNickname(writer.getNickname())
                            .writerProfileImage(writer.getProfileImage())
                            .content(originalComment.getContent())
                            .isLike(checkUserLikedComment(userId, originalComment))
                            .likeCount(getLikeCount(originalComment.getId()))
                            .writeAt(calculateWriteAt(originalComment.getWriteAt()))
                            .build();
                })
                .toList();
    }

    public static String calculateWriteAt(LocalDateTime writeAt) {
        LocalDateTime now = LocalDateTime.now();
        long hoursDifference = ChronoUnit.MINUTES.between(writeAt, now);

        if (hoursDifference < 1) {
            long secondsDifference = ChronoUnit.SECONDS.between(writeAt, now);
            return secondsDifference + "초 전";
        } else if (hoursDifference < 60) {
            long minutesDifference = ChronoUnit.MINUTES.between(writeAt, now);
            return minutesDifference + "분 전";
        } else if (hoursDifference < 60*24) {
            return hoursDifference + "시간 전";
        } else {
            return writeAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }
    }


    private boolean checkUserLikedComment(UUID userId, Comment comment) {
        return comment.getLikes().stream()
                .anyMatch(commentLike -> commentLike.getUsers().getId().equals(userId));
    }

    @Transactional
    public CommentDTO.Response createComment(UUID userId, UUID stoneId, CommentDTO.Request commentRequest) {
        Users writer = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage() + userId));
        Stone stone = stoneService.getStoneByStoneIdAfterFinalDate(stoneId);

        Comment comment = Comment.builder()
                .stone(stone)
                .writer(writer)
                .content(commentRequest.getContent())
                .build();

        // Comment 저장 후 반환
        Comment savedComment = save(comment);

        return CommentDTO.Response.builder()
                .id(savedComment.getId())
                .stoneId(savedComment.getStone().getId())
                .writerId(savedComment.getWriter().getId())
                .content(savedComment.getContent())
                .writeAt(savedComment.getWriteAt())
                .build();
    }

    public Integer getLikeCount(UUID commentId) {
        Optional<Comment> optionalComment = Optional.ofNullable(getCommentByCommentId(commentId));
        return optionalComment.map(comment -> comment.getLikes().size()).orElse(0);
    }

    @Transactional
    public CommentLikeDTO toggleCommentLike(UUID userId, UUID commentId) {
        Comment comment = getCommentByCommentId(commentId);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage() + userId));

        Optional<CommentLike> existingLike = commentLikeService.findByUsersAndComment(user, comment);

        if (existingLike.isPresent()) {
            commentLikeService.delete(existingLike.get());
            return CommentLikeDTO.builder()
                    .commentId(commentId)
                    .isLike(false)
                    .build();
        } else {
            CommentLike newLike = CommentLike.builder()
                    .users(user)
                    .comment(comment)
                    .build();

            commentLikeService.save(newLike);
            return CommentLikeDTO.builder()
                    .commentId(commentId)
                    .isLike(true)
                    .build();
        }
    }

    public Comment getCommentByCommentId(UUID commentId){
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND.getMessage() + commentId));
    }

    public List<Comment> getCommentsByStoneId(UUID stoneId){
        return commentRepository.findByStoneId(stoneId);
    }

    @Transactional
    public Comment save(Comment comment){
        return commentRepository.save(comment);
    }
}

