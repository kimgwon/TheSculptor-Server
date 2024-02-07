package backend.sculptor.domain.comment.service;

import backend.sculptor.domain.comment.entity.Comment;
import backend.sculptor.domain.comment.entity.CommentLike;
import backend.sculptor.domain.comment.repository.CommentLikeRepository;
import backend.sculptor.domain.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;

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
