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
import com.example.demo.response.board.CommunityResponse;
import com.example.demo.response.board.BoardResponse;
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

        try {

            User user = findUser(userId);

            Community community = Community.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .writer(user.getNickName())
                    .build();

            return (communityMapper.createBoard(community) == 1);
        } catch (Exception e) {
            log.error(String.valueOf(e));
            return false;
        }
    }


    public List<CommunityResponse> getBoardList(String type, String keyword, String sort) {

        List<CommunityResponse> collect = getSortList(type, keyword, sort);

        log.info("collect={}", collect);

        return collect;
    }

    public BoardResponse getBoard(Integer boardId) {
//        Community findBoard = findBoard(boardId);

        Community findBoard =  communityMapper.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

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
        User user = userMapper.findById(userId).get();
        return user.getName();
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

    private List<CommunityResponse> getSortList(String type, String keyword, String sort) {

        List<CommunityResponse> collect = communityMapper.findAll(sort).stream()
                .map(community -> CommunityResponse.builder()
                        .id(community.getId())
                        .title(community.getTitle())
                        .writer(community.getWriter())
                        .viewCount(community.getViewCount())
                        .likeCount(community.getLikeCount())
                        .inserted(getFormatted(community))
                        .build()).collect(Collectors.toList());

        return collect;
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

    private List<CommunityResponse> getSortList1(String type, String keyword, String sort) {
        List<CommunityResponse> collect = null;

        if (sort.equals("id")) {
            collect = communityMapper.findAll(sort).stream()
                    .map(community -> CommunityResponse.builder()
                            .id(community.getId())
                            .title(community.getTitle())
                            .writer(community.getWriter())
                            .viewCount(community.getViewCount())
                            .likeCount(community.getLikeCount())
                            .inserted(getFormatted(community))
                            .build()).collect(Collectors.toList());
        } else if (sort.equals("like")) {
            collect = communityMapper.findAllSortByLike().stream()
                    .map(community -> CommunityResponse.builder()
                            .id(community.getId())
                            .title(community.getTitle())
                            .writer(community.getWriter())
                            .likeCount(community.getLikeCount())
                            .inserted(getFormatted(community))
                            .build()).collect(Collectors.toList());
        } else if (sort.equals("comment")) {
            collect = communityMapper.findAllSortByComment().stream()
                    .map(community -> CommunityResponse.builder()
                            .id(community.getId())
                            .title(community.getTitle())
                            .writer(community.getWriter())
                            .likeCount(community.getLikeCount())
                            .inserted(getFormatted(community))
                            .build()).collect(Collectors.toList());
        } else if (sort.equals("view")) {
            collect = communityMapper.findAllSortByView().stream()
                    .map(community -> CommunityResponse.builder()
                            .id(community.getId())
                            .title(community.getTitle())
                            .writer(community.getWriter())
                            .likeCount(community.getLikeCount())
                            .inserted(getFormatted(community))
                            .build()).collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("원하시는 정렬을 할 수 없습니다.");
        }
        return collect;
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
