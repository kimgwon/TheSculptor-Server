package backend.sculptor.domain.comment.service;

import backend.sculptor.domain.comment.dto.CommentLikeDTO;
import backend.sculptor.domain.comment.entity.Comment;
import backend.sculptor.domain.comment.entity.CommentLike;
import backend.sculptor.domain.comment.repository.CommentLikeRepository;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.global.exception.ErrorCode;
import backend.sculptor.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLikeService {
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentService commentService;

    @Transactional
    public CommentLikeDTO toggleCommentLike(UUID userId, UUID commentId) {
        Comment comment = commentService.getCommentByCommentId(commentId);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage() + userId));

        Optional<CommentLike> existingLike = findByUsersAndComment(user, comment);

        if (existingLike.isPresent()) {
            delete(existingLike.get());
            return CommentLikeDTO.builder()
                    .commentId(commentId)
                    .isLike(false)
                    .build();
        } else {
            CommentLike newLike = CommentLike.builder()
                    .users(user)
                    .comment(comment)
                    .build();

            save(newLike);
            return CommentLikeDTO.builder()
                    .commentId(commentId)
                    .isLike(true)
                    .build();
        }
    }

    public void save(CommentLike like){
        commentLikeRepository.save(like);
    }

    public void delete(CommentLike like){
        commentLikeRepository.delete(like);
    }

    public Optional<CommentLike> findByUsersAndComment(Users user, Comment comment) {
        return commentLikeRepository.findByUsersAndComment(user, comment);
    }
}
