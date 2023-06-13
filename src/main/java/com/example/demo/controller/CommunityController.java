package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
import com.example.demo.request.board.CreateBoardRequest;
import com.example.demo.request.board.UpdateBoardRequest;
import com.example.demo.request.comment.CommentRequest;
import com.example.demo.response.board.BoardResponse;
import com.example.demo.response.board.CommunityResponse;
import com.example.demo.response.comment.CommentResponse;
import com.example.demo.service.CommunityService;
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
    public CommunityResponse getBoardList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                          @RequestParam(value = "type", defaultValue = "all") String type,
                                          @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                          @RequestParam(value = "sort", defaultValue = "id") String sort) {

        log.info("boardList -> page={}, type={}, keyword={}, sort={}", page, type, keyword, sort);

        return communityService.getBoardList(page, type, keyword, sort);
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
        Boolean ok = communityService.updateBoard(authUser.getUserId(), boardId, request);

        if (ok) {
            return new ResponseEntity<>("게시글이 수정되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("게시글 수정에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Integer boardId, AuthUser authUser) {
        Boolean ok = communityService.deleteBoard(authUser.getUserId(), boardId);

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
    public ResponseEntity<String> addComment(@PathVariable Integer boardId, @RequestBody CommentRequest request, AuthUser authUser) {
        Boolean ok = communityService.addComment(authUser.getUserId(), boardId, request.getContent());

        if (ok) {
            return new ResponseEntity<>("댓글이 등록되었습니다.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("댓글 등록에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/board/{boardId}/comment")
    public List<CommentResponse> getCommentList(@PathVariable Integer boardId) {
        return communityService.getCommentList(boardId);
    }

    @PutMapping("/board/{boardId}/comment")
    public ResponseEntity<String> modifyComment(@PathVariable Integer boardId, @RequestBody CommentRequest request, AuthUser authUser) {
        log.info("comment modify request={}", request);
        Boolean ok = communityService.modifyComment(authUser.getUserId(), boardId, request);

        if (ok) {
            return new ResponseEntity<>("댓글이 수정되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("댓글 수정에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/board/{boardId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Integer boardId, @PathVariable Integer commentId, AuthUser authUser) {
        Boolean ok = communityService.deleteComment(authUser.getUserId(), boardId, commentId);

        if (ok) {
            return new ResponseEntity<>("댓글이 삭제되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("댓글 삭제에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

//    @PostMapping
//    public void createBoar1(@RequestBody CreateBoardRequest request, AuthUser authUser) {
//        log.info("request={}", request);
//
//        communityService.createBoard1(authUser.getUserId());
//    }

//        public void createBoard1(Long userId) {
//
//        User user = findUser(userId);
//
//        for (int i = 0; i < 3000; i++) {
//            Community community = Community.builder()
//                    .title("제목" + i)
//                    .content("내용" + i)
//                    .writer(user.getNickName())
//                    .build();
//            communityMapper.createBoard(community);
//        }
//    }

}
