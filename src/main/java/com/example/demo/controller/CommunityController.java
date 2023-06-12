package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
import com.example.demo.request.board.CreateBoardRequest;
import com.example.demo.request.board.UpdateBoardRequest;
import com.example.demo.response.CommunityResponse;
import com.example.demo.response.board.BoardResponse;
import com.example.demo.response.board.CommentResponse;
import com.example.demo.service.CommunityService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    /**
     * 게시글 기능
     */
    @PostMapping
    public ResponseEntity<String> createBoard(@RequestBody CreateBoardRequest request, AuthUser authUser) {
        log.info("request={}", request);

        Boolean ok = communityService.createBoard(authUser.getUserId(), request);

        if (ok) {
            return new ResponseEntity<>("게시글이 등록되었습니다.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("게시글 작성에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<CommunityResponse> getBoardList(@RequestParam(defaultValue = "all") String type,
                                                @RequestParam(defaultValue = "") String keyword,
                                                @RequestParam(defaultValue = "id") String sort) {

        log.info("boardList -> type={}, keyword={}, sort={}", type, keyword, sort);

        List<CommunityResponse> communityList = communityService.getBoardList(type, keyword, sort);
        return communityList;
    }

    @GetMapping("/getWriter")
    public String getWriter(AuthUser authUser) {
        return communityService.getWriter(authUser.getUserId());
    }

    @GetMapping("/board/{boardId}")
    public BoardResponse getBoard(@PathVariable Integer boardId) {
        return communityService.getBoard(boardId);
    }

    @PatchMapping("/board/{boardId}")
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

    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Integer boardId, AuthUser authUser) {
        Boolean ok = communityService.deleteBoard(boardId, authUser.getUserId());

        if (ok) {
            return new ResponseEntity<>("게시글이 삭제되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("게시글 삭제에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 추천 기능
     */
    @PatchMapping("/board/{boardId}/likeUp")
    public Integer updateLikeUp(@PathVariable Integer boardId, AuthUser authUser) {
        return communityService.updateLikeUp(boardId);
    }

    @PatchMapping("/board/{boardId}/likeDown")
    public Integer updateLikeDown(@PathVariable Integer boardId, AuthUser authUser) {
        return communityService.updateLikeDown(boardId);
    }

    /**
     * 댓글 기능
     */
    @PostMapping("/board/{boardId}/comment")
    public void addComment(@PathVariable Integer boardId, @RequestBody CreateCommentRequest request, AuthUser authUser) {
        communityService.addComment(boardId, authUser.getUserId(), request);
    }

    @GetMapping("/board/{boardId}/comment")
    public List<CommentResponse> getCommentList(@PathVariable Integer boardId) {
        return communityService.getCommentList(boardId);
    }

    @PutMapping("/board/{boardId}/comment")
    public void modifyComment(@PathVariable Integer boardId, AuthUser authUser) {
        communityService.modifyComment(boardId, authUser.getUserId());
    }

    @DeleteMapping("/board/{boardId}/comment")
    public void deleteComment(@PathVariable Integer boardId, AuthUser authUser) {
        communityService.deleteComment(boardId, authUser.getUserId());
    }



    @Data
    public static class CreateCommentRequest {
        private String content;
    }


}
