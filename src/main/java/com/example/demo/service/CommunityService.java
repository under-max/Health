package com.example.demo.service;

import com.example.demo.entity.Community;
import com.example.demo.entity.User;
import com.example.demo.exception.UserNotFound;
import com.example.demo.mapper.CommunityMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.request.board.CreateBoardRequest;
import com.example.demo.request.board.UpdateBoardRequest;
import com.example.demo.response.CommunityResponse;
import com.example.demo.response.board.BoardResponse;
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


    public List<CommunityResponse> getBoardList(String type, String keyword, String sort) {

        List<CommunityResponse> collect = getSortList(type, keyword, sort);

        log.info("collect={}", collect);

        return collect;
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

    public Boolean updateBoard(Integer boardId, Long userId, UpdateBoardRequest request) {
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

    public Boolean deleteBoard(Integer boardId, Long userId) {
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
}
