package backend.sculptor.domain.comment.service;

import backend.sculptor.domain.comment.dto.CommentDTO;
import backend.sculptor.domain.comment.entity.Comment;
import backend.sculptor.domain.comment.repository.CommentRepository;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.repository.StoneRepository;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.global.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final StoneRepository stoneRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, StoneRepository stoneRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.stoneRepository = stoneRepository;
        this.userRepository = userRepository;
    }

    public List<CommentDTO.Info> getComments(UUID userId, UUID ownerId, UUID stoneId) {
        stoneRepository.findById(stoneId).orElseThrow(() -> new NotFoundException("Stone not found"));
        userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("User not found"));

        List<Comment> comments = commentRepository.findByStoneId(stoneId);

        return convertToCommentResponse(userId, comments);
    }

    private List<CommentDTO.Info> convertToCommentResponse(UUID userId, List<Comment> comments) {
        return comments.stream()
                .map(originalComment -> {
                    CommentDTO.Info comment = new CommentDTO.Info();
                    Users writer = originalComment.getWriter();

                    comment.setId(originalComment.getId());
                    comment.setWriterId(writer.getId());
                    comment.setWriterNickname(writer.getNickname());
                    comment.setContent(originalComment.getContent());
                    comment.setLike(checkUserLikedComment(userId, originalComment));
                    comment.setWriteAt(originalComment.getWriteAt());

                    return comment;
                })
                .collect(Collectors.toList());
    }

    private boolean checkUserLikedComment(UUID userId, Comment comment) {
        return comment.getLikes().stream()
                .anyMatch(commentLike -> commentLike.getUsers().getId().equals(userId));
    }

    public CommentDTO.Response createComment(UUID userId, UUID ownerId, UUID stoneId, CommentDTO.Request commentRequest) {
        Users writer = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Stone stone = stoneRepository.findById(stoneId)
                .orElseThrow(() -> new NotFoundException("Stone not found"));
        userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("User not found"));

        Comment comment = new Comment();
        comment.setStone(stone);
        comment.setWriter(writer);
        comment.setContent(commentRequest.getContent());
        comment.setWriteAt(LocalDateTime.now());

        // Comment 저장 후 반환
        Comment savedComment = commentRepository.save(comment);

        CommentDTO.Response commentDTO = new CommentDTO.Response();
        commentDTO.setId(savedComment.getId());
        commentDTO.setStoneId(savedComment.getStone().getId());
        commentDTO.setWriterId(savedComment.getWriter().getId());
        commentDTO.setContent(savedComment.getContent());
        commentDTO.setWriteAt(savedComment.getWriteAt());

        return commentDTO;
    }
}

