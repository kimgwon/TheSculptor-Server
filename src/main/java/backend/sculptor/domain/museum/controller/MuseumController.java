package backend.sculptor.domain.museum.controller;

import backend.sculptor.domain.comment.dto.CommentDTO;
import backend.sculptor.domain.comment.dto.CommentLikeDTO;
import backend.sculptor.domain.comment.service.CommentLikeService;
import backend.sculptor.domain.comment.service.CommentService;
import backend.sculptor.domain.museum.dto.MuseumDTO;
import backend.sculptor.domain.museum.dto.MuseumDetailDTO;
import backend.sculptor.domain.museum.dto.MuseumProfileDTO;
import backend.sculptor.domain.museum.service.MuseumDetailService;
import backend.sculptor.domain.museum.service.MuseumProfoileService;
import backend.sculptor.domain.museum.service.MuseumService;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.global.api.APIBody;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/museum")
public class MuseumController {
    private final MuseumService museumService;
    private final MuseumDetailService museumDetailService;
    private final MuseumProfoileService museumProfoileService;
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    @GetMapping("/users/{ownerId}")
    public APIBody<MuseumDTO> getMuseumInfo(@CurrentUser SessionUser user, @PathVariable UUID ownerId) {
        MuseumDTO museumDTO = museumService.getMuseumInfo(ownerId, user.getId());
        return APIBody.of(HttpStatus.OK.value(), "박물관 조회 성공", museumDTO);
    }

    @GetMapping("/stones/{stoneId}")
    public APIBody<MuseumDetailDTO> getStoneDetail(
            @CurrentUser SessionUser user,
            @PathVariable UUID stoneId) {
        MuseumDetailDTO museumDetail = museumDetailService.getMuseumDetailInfo(user.getId(), stoneId);
        return APIBody.of(HttpStatus.OK.value(), "박물관 세부 조회 성공", museumDetail);
    }

    @GetMapping("/stones/{stoneId}/comments")
    public APIBody<Map<String, List<CommentDTO.Info>>> getComments(
            @CurrentUser SessionUser user,
            @PathVariable UUID stoneId) {
        List<CommentDTO.Info> comments = commentService.getComments(user.getId(), stoneId);
        Map<String, List<CommentDTO.Info>> data = new HashMap<>();
        data.put("comments", comments);

        return APIBody.of(HttpStatus.OK.value(), "방명록 조회 성공", data);
    }
    
    @PostMapping("/stones/{stoneId}/comments")
    public APIBody<CommentDTO.Response> saveComment(
            @CurrentUser SessionUser user,
            @PathVariable UUID stoneId,
            @RequestBody CommentDTO.Request commentRequest) {
        CommentDTO.Response savedComment = commentService.createComment(user.getId(), stoneId, commentRequest);
        return APIBody.of(HttpStatus.OK.value(), "방명록 작성 성공", savedComment);
    }

    @PatchMapping("/comments/{commentId}/like")
    public APIBody<CommentLikeDTO> toggleCommentLike(
            @CurrentUser SessionUser user,
            @PathVariable UUID commentId) {
        CommentLikeDTO commentLike = commentLikeService.toggleCommentLike(user.getId(), commentId);

        return APIBody.of(HttpStatus.OK.value(), "방명록 좋아요 상태 변경 성공", commentLike);
    }

    @GetMapping("/profile/user")
    public APIBody<MuseumProfileDTO.User> getMuseumProfileUser(
            @CurrentUser SessionUser user) {
        MuseumProfileDTO.User profileUser = museumProfoileService.getProfileUser(user.getId());
        return APIBody.of(200, "박물관 프로필 유저 정보 조회 성공", profileUser);
    }

    @GetMapping("/profile/stones")
    public APIBody<Map<String, List<MuseumProfileDTO.Stone>>> getMuseumProfileStones(
            @CurrentUser SessionUser user) {
        List<MuseumProfileDTO.Stone> profileStones = museumProfoileService.getProfileStones(user.getId());
        Map<String, List<MuseumProfileDTO.Stone>> data = new HashMap<>();
        data.put("stones", profileStones);

        return APIBody.of(200, "박물관 프로필 돌 정보 조회 성공", data);
    }

    @PatchMapping("/profile/user")
    public APIBody<MuseumProfileDTO.User> editMuseumProfileUser(
            @CurrentUser SessionUser user,
            @RequestBody MuseumProfileDTO.User userProfile) {
        MuseumProfileDTO.User editUser = museumProfoileService.editProfile(user.getId(), userProfile);
        return APIBody.of(200, "박물관 프로필 유저 정보 수정 성공", editUser);
    }

    @DeleteMapping("/profile/stones/{stoneId}")
    public APIBody<MuseumProfileDTO.User> deleteMuseumProfileStone(
            @CurrentUser SessionUser user,
            @PathVariable UUID stoneId) {
        museumProfoileService.deleteStone(user.getId(), stoneId);
        return APIBody.of(200, "박물관 돌 삭제 성공", null);
    }
}
