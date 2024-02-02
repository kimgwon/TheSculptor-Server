/*
package backend.sculptor.domain.museum.controller;

import backend.sculptor.domain.museum.dto.Museum;
import backend.sculptor.domain.museum.dto.MuseumDetail;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import backend.sculptor.domain.museum.service.MuseumDetailService;
import backend.sculptor.domain.museum.service.MuseumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/museum")
public class MuseumController {
    private final MuseumService museumService;
    private final MuseumDetailService museumDetailService;

    @Autowired
    public MuseumController(MuseumService museumService, MuseumDetailService museumDetailService) {
        this.museumService = museumService;
        this.museumDetailService = museumDetailService;
    }

    @GetMapping("users/{ownerId}")
    public ResponseEntity<Museum> getMuseumInfo(@PathVariable UUID ownerId, @CurrentUser SessionUser user) {
        Museum museum = museumService.getMuseumInfo(ownerId, user.getId());

        return ResponseEntity.ok(museum);
    }

    @GetMapping("users/{ownerId}/stones/{stoneId}")
    public ResponseEntity<MuseumDetail> getStoneDetail(@PathVariable UUID ownerId, @PathVariable UUID stoneId) {
        Optional<MuseumDetail> optionalMuseumDetail = museumDetailService.getMuseumDetailInfo(ownerId, stoneId);

        return optionalMuseumDetail
                .map(museumDetail -> ResponseEntity.ok().body(museumDetail))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

 */