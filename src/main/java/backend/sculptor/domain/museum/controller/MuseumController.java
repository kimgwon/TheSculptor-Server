package backend.sculptor.domain.museum.controller;

import backend.sculptor.domain.comment.dto.CommentDTO;
import backend.sculptor.domain.comment.service.CommentService;
import backend.sculptor.domain.museum.dto.MuseumDTO;
import backend.sculptor.domain.museum.dto.MuseumDetailDTO;
import backend.sculptor.domain.museum.service.MuseumDetailService;
import backend.sculptor.domain.museum.service.MuseumService;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.global.api.APIBody;
import backend.sculptor.global.exception.NotFoundException;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/museum")
public class MuseumController {
    private final MuseumService museumService;
    private final MuseumDetailService museumDetailService;
    private final CommentService commentService;

    @Autowired
    public MuseumController(MuseumService museumService, MuseumDetailService museumDetailService, CommentService commentService) {
        this.museumService = museumService;
        this.museumDetailService = museumDetailService;
        this.commentService = commentService;
    }

    @GetMapping("/users/{ownerId}")
    public ResponseEntity<APIBody<MuseumDTO>> getMuseumInfo(@CurrentUser SessionUser user, @PathVariable UUID ownerId) {
        MuseumDTO museumDTO = museumService.getMuseumInfo(ownerId, user.getId());

        return ResponseEntity.ok(APIBody.of(200, "박물관 조회 성공", museumDTO));
    }

    @GetMapping("/users/{ownerId}/stones/{stoneId}")
    public ResponseEntity<APIBody<MuseumDetailDTO>> getStoneDetail(
            @CurrentUser SessionUser user,
            @PathVariable UUID ownerId,
            @PathVariable UUID stoneId) {
        try {
            MuseumDetailDTO museumDetail = museumDetailService.getMuseumDetailInfo(user.getId(), ownerId, stoneId);
            return ResponseEntity.ok(APIBody.of(200, "박물관 세부 조회 성공", museumDetail));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIBody.of(404, e.getMessage(), null));
        }
    }

    @GetMapping("/users/{ownerId}/stones/{stoneId}/comments")
    public ResponseEntity<APIBody<List<CommentDTO.Info>>> getComments(
            @CurrentUser SessionUser user,
            @PathVariable UUID ownerId,
            @PathVariable UUID stoneId) {
        try {
            List<CommentDTO.Info> comments = commentService.getComments(user.getId(), ownerId, stoneId);
            return ResponseEntity.ok(APIBody.of(200, "방명록 조회 성공", comments));
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIBody.of(404, e.getMessage(), null));
        }
    }
    
    @PostMapping("/users/{ownerId}/stones/{stoneId}/comments")
    public ResponseEntity<APIBody<CommentDTO.Response>> saveComment(
            @CurrentUser SessionUser user,
            @PathVariable UUID ownerId,
            @PathVariable UUID stoneId,
            @RequestBody CommentDTO.Request commentRequest) {
        try {
            CommentDTO.Response savedComment = commentService.createComment(user.getId(), ownerId, stoneId, commentRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(APIBody.of(200, "방명록 작성 성공", savedComment));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIBody.of(404, e.getMessage(), null));
        }
    }
}
