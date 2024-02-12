package backend.sculptor.domain.comment.repository;

import backend.sculptor.domain.comment.entity.Comment;
import backend.sculptor.domain.comment.entity.CommentLike;
import backend.sculptor.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, UUID> {
    Optional<CommentLike> findByUsersAndComment(Users users, Comment comment);
}