package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
import com.example.demo.response.CommunityResponse;
import com.example.demo.service.CommunityService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("/community")
    public ResponseEntity<String> createBoard(@RequestBody CreateBoardRequest request, AuthUser authUser) {
        log.info("request={}", request);

        Boolean ok = communityService.createBoard(authUser.getUserId(), request);

        if (ok) {
            return new ResponseEntity<>("게시글이 등록되었습니다.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("게시글 작성에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/community/getWriter")
    public String getWriter(AuthUser authUser) {
        return communityService.getWriter(authUser.getUserId());
    }

    @GetMapping("/community/board/{boardId}")
    public BoardResponse getBoard(@PathVariable Integer boardId) {
        return communityService.getBoard(boardId);
    }

    @GetMapping("/community")
    public List<CommunityResponse> getBoardList(@RequestParam(defaultValue = "all") String type,
                                                @RequestParam(defaultValue = "") String keyword,
                                                @RequestParam(defaultValue = "id") String sort) {

        log.info("boardList -> type={}, keyword={}, sort={}", type, keyword, sort);

        List<CommunityResponse> communityList = communityService.getBoardList(type, keyword, sort);
        return communityList;
    }

    @PatchMapping("/community/board/{boardId}/likeUp")
    public Integer updateLikeUp(@PathVariable Integer boardId, AuthUser authUser) {
        return communityService.updateLikeUp(boardId);
    }

    @PatchMapping("/community/board/{boardId}/likeDown")
    public Integer updateLikeDown(@PathVariable Integer boardId, AuthUser authUser) {
        return communityService.updateLikeDown(boardId);
    }

    @PatchMapping("/community/board/{boardId}")
    public ResponseEntity<String> updateBoard(@PathVariable Integer boardId,
                              @RequestBody UpdateBoardRequest request,
                              AuthUser authUser) {

        log.info("update request={}", request);
        Boolean ok = communityService.updateBoard(boardId, authUser.getUserId(), request);

        if (ok) {
            return new ResponseEntity<>("게시글이 수정되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("게시글 수정에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/community/board/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Integer boardId, AuthUser authUser) {
        Boolean ok = communityService.deleteBoard(boardId, authUser.getUserId());

        if (ok) {
            return new ResponseEntity<>("게시글이 삭제되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("게시글 삭제에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @Data
    public static class CreateBoardRequest {
        private String title;
        private String content;
    }

    @Data
    public static class UpdateBoardRequest {
        private String title;
        private String content;
    }

    @Data
    public static class BoardResponse {
        private String title;
        private String content;
        private String writer;
        private Integer viewCount;
        private Integer likeCount;
        private String inserted;

        @Builder
        public BoardResponse(String title, String content, String writer, Integer viewCount, Integer likeCount, String inserted) {
            this.title = title;
            this.content = content;
            this.writer = writer;
            this.viewCount = viewCount;
            this.likeCount = likeCount;
            this.inserted = inserted;
        }
    }
}
