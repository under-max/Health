package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
import com.example.demo.request.board.CreateBoardRequest;
import com.example.demo.request.board.UpdateBoardRequest;
import com.example.demo.request.comment.CommentRequest;
import com.example.demo.response.ErrorResponse;
import com.example.demo.response.board.BoardResponse;
import com.example.demo.response.board.CommunityResponse;
import com.example.demo.response.comment.CommentResponse;
import com.example.demo.service.CommunityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/community/board")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgExHandler(MethodArgumentNotValidException e) {
        log.info("methodArgExHandler 호출");

        BindingResult bindingResult = e.getBindingResult();

        FieldError title = bindingResult.getFieldError("title");
        FieldError content = bindingResult.getFieldError("content");

        String titleDefaultMessage = title.getDefaultMessage();
        String contentDefaultMessage = content.getDefaultMessage();

        Map<String, String> messages = new HashMap<>();

        if (StringUtils.hasText(titleDefaultMessage)) {
            messages.put("title", titleDefaultMessage);
        }

        if (StringUtils.hasText(contentDefaultMessage)) {
            messages.put("content", contentDefaultMessage);
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .validation(messages)
                .build();

        log.info("errorResponse={}", errorResponse);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runTimeExHandler(RuntimeException e) {

        log.info("runTimeExHandler 호출");
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("BAD")
                .message(e.getMessage())
                .build();

        log.info("errorResponse={}", errorResponse);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 게시글 기능
     */
    @PostMapping
    public ResponseEntity<String> createBoard(@Valid @RequestBody CreateBoardRequest request, AuthUser authUser) {
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

        log.info("getCommunityList -> page={}, type={}, keyword={}, sort={}", page, type, keyword, sort);

        return communityService.getBoardList(page, type, keyword, sort);
    }

    @GetMapping("/getWriter")
    public String getWriter(AuthUser authUser) {
        return communityService.getWriter(authUser.getUserId());
    }

    @GetMapping("/{boardId}/get")
    public BoardResponse getBoard(@PathVariable Integer boardId) {
        return communityService.getBoard(boardId);
    }

    @GetMapping("/{boardId}")
    public BoardResponse getBoard(@PathVariable Integer boardId, AuthUser authUser) {
        return communityService.getBoard(authUser.getUserId(), boardId);
    }

    @PatchMapping("/{boardId}")
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

    @DeleteMapping("/{boardId}")
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
    @PatchMapping("/{boardId}/likeUp")
    public Integer updateLikeUp(@PathVariable Integer boardId, AuthUser authUser) {
        return communityService.updateLikeUp(boardId);
    }

    @PatchMapping("/{boardId}/likeDown")
    public Integer updateLikeDown(@PathVariable Integer boardId, AuthUser authUser) {
        return communityService.updateLikeDown(boardId);
    }

    /**
     * 댓글 기능
     */
    @PostMapping("/{boardId}/comment")
    public ResponseEntity<String> addComment(@PathVariable Integer boardId, @Valid @RequestBody CommentRequest request, AuthUser authUser) {
        Boolean ok = communityService.addComment(authUser.getUserId(), boardId, request.getContent());

        if (ok) {
            return new ResponseEntity<>("댓글이 등록되었습니다.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("댓글 등록에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{boardId}/comment")
    public List<CommentResponse> getCommentList(@PathVariable Integer boardId) {
        return communityService.getCommentList(boardId);
    }

    @PutMapping("/{boardId}/comment")
    public ResponseEntity<String> modifyComment(@PathVariable Integer boardId, @Valid @RequestBody CommentRequest request, AuthUser authUser) {
        log.info("comment modify request={}", request);
        Boolean ok = communityService.modifyComment(authUser.getUserId(), boardId, request);

        if (ok) {
            return new ResponseEntity<>("댓글이 수정되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("댓글 수정에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Integer boardId, @PathVariable Integer commentId, AuthUser authUser) {
        Boolean ok = communityService.deleteComment(authUser.getUserId(), boardId, commentId);

        if (ok) {
            return new ResponseEntity<>("댓글이 삭제되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("댓글 삭제에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

}
