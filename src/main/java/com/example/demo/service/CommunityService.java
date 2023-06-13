package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Community;
import com.example.demo.entity.User;
import com.example.demo.exception.UserNotFound;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.mapper.CommunityMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.request.board.CreateBoardRequest;
import com.example.demo.request.board.UpdateBoardRequest;
import com.example.demo.request.comment.CommentRequest;
import com.example.demo.request.comment.CreateCommentDto;
import com.example.demo.request.comment.DeleteCommentDto;
import com.example.demo.request.comment.UpdateCommentDto;
import com.example.demo.response.board.BoardPageInfo;
import com.example.demo.response.board.BoardResponse;
import com.example.demo.response.board.CommunityResponse;
import com.example.demo.response.board.CommunityResponseDto;
import com.example.demo.response.comment.CommentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityMapper communityMapper;
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    public Boolean createBoard(Long userId, CreateBoardRequest request) {

        User user = findUser(userId);

        Community community = Community.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writer(user.getNickName())
                .build();

        return (communityMapper.createBoard(community) == 1);
    }

    public CommunityResponse getBoardList(Integer page, String type, String keyword, String sort) {

        try {
            CommunityResponse response = getSortList(page, type, keyword, sort);
            log.info("getBoardList() response={}", response);
            return response;

        } catch (Exception e) {
            log.error("getBoardList() error", e);
            throw new RuntimeException("오류 해결 중입니다.");
        }
    }

    public BoardResponse getBoard(Integer boardId) {

        Community findBoard = findBoard(boardId);

        BoardResponse response = BoardResponse.builder()
                .title(findBoard.getTitle())
                .content(findBoard.getContent())
                .writer(findBoard.getWriter())
                .viewCount(findBoard.getViewCount())
                .likeCount(findBoard.getLikeCount())
                .inserted(getFormatted(findBoard))
                .build();

        log.info("getBoard() response={}", response);

        communityMapper.viewCount(boardId);

        return response;
    }

    public String getWriter(Long userId) {
        User user = findUser(userId);
        return user.getNickName();
    }

    public Integer updateLikeUp(Integer boardId) {
        communityMapper.updateLikeUp(boardId);
        Community findBoard = findBoard(boardId);
        return findBoard.getLikeCount();
    }

    public Integer updateLikeDown(Integer boardId) {
        communityMapper.updateLikeDown(boardId);
        Community findBoard = findBoard(boardId);
        return findBoard.getLikeCount();
    }

    public Boolean updateBoard(Long userId, Integer boardId, UpdateBoardRequest request) {
        Integer cnt = 0;
        User findUser = findUser(userId);
        String writer = findUser.getNickName();
        Community findBoard = findBoard(boardId);

        if (findBoard.getWriter().equals(writer)) {
            cnt = communityMapper.updateBoard(boardId, writer, request.getTitle(), request.getContent());
        } else {
            throw new IllegalArgumentException("작성자만 수정이 가능합니다.");
        }

        return cnt == 1;
    }

    public Boolean deleteBoard(Long userId, Integer boardId) {
        Integer cnt = 0;
        User findUser = findUser(userId);
        String writer = findUser.getNickName();
        Community findBoard = findBoard(boardId);

        if (findBoard.getWriter().equals(writer)) {
            cnt = communityMapper.deleteBoard(boardId, writer);
        }

        return cnt == 1;
    }

    private CommunityResponse getSortList(Integer page, String type, String keyword, String sort) {

        // 한 페이지 당 row 개수
        Integer rowPage = 10;

        // sql query limit 절에 사용할 시작 인덱스
        Integer startIndex = (page - 1) * rowPage;

        // 전체 records 개수
        Integer records = communityMapper.countAll(type, keyword);
        log.info("list records=[{}]", records);

        // 마지막 페이지 번호
        Integer lastPageNumber = (records - 1) / rowPage + 1;

        // 왼쪽 페이지 번호
        Integer leftPageNum = Math.max(page - 5, 1);

        // 오른쪽 페이지 번호
        Integer rightPageNum = Math.min(leftPageNum + 9, lastPageNumber);

        BoardPageInfo pageInfo = BoardPageInfo.builder()
                .leftPageNumber(leftPageNum)
                .rightPageNumber(rightPageNum)
                .page(page)
                .lastPageNumber(lastPageNumber)
                .records(records)
                .build();

        List<CommunityResponseDto> list = communityMapper.findAll(type, keyword, sort, startIndex, rowPage);

        return CommunityResponse.builder()
                .pageInfo(pageInfo)
                .list(list)
                .build();
    }

    private User findUser(Long userId) {
        return userMapper.findById(userId).orElseThrow(UserNotFound::new);
    }

    private Community findBoard(Integer boardId) {
        return communityMapper.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }

    private String getFormatted(Community findBoard) {
        return findBoard.getInserted().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 댓글 기능
     */
    public Boolean addComment(Long userId, Integer boardId, String content) {

        User user = findUser(userId);

        CreateCommentDto createCommentDto = CreateCommentDto.builder()
                .boardId(boardId)
                .writer(user.getNickName())
                .content(content)
                .build();

        log.info("boardId={}, writer={}, content={}", createCommentDto.getBoardId(), createCommentDto.getWriter(), createCommentDto.getContent());

        return (commentMapper.addComment(createCommentDto) == 1);
    }

    public List<CommentResponse> getCommentList(Integer boardId) {

        List<Comment> commentList = commentMapper.findAllByBoardId(boardId);

        List<CommentResponse> responseList = commentList.stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .writer(comment.getWriter())
                        .content(comment.getContent())
                        .inserted(comment.getInserted().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .build()).collect(Collectors.toList());

        log.info("commentResponseList={}", responseList);
        return responseList;
    }

    public Boolean modifyComment(Long userId, Integer boardId, CommentRequest request) {

        User user = findUser(userId);

        UpdateCommentDto updateCommentDto = UpdateCommentDto.builder()
                .boardId(boardId)
                .commentId(request.getCommentId())
                .writer(user.getNickName())
                .content(request.getContent())
                .build();

        return (commentMapper.updateComment(updateCommentDto) == 1);
    }

    public Boolean deleteComment(Long userId, Integer boardId, Integer commentId) {

        User user = findUser(userId);

        DeleteCommentDto deleteCommentDto = DeleteCommentDto.builder()
                .boardId(boardId)
                .commentId(commentId)
                .writer(user.getNickName())
                .build();

        return (commentMapper.deleteComment(deleteCommentDto) == 1);
    }
}
