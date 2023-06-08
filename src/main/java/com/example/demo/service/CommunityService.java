package com.example.demo.service;

import com.example.demo.controller.CommunityController;
import com.example.demo.entity.Community;
import com.example.demo.entity.User;
import com.example.demo.exception.UserNotFound;
import com.example.demo.mapper.CommunityMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.response.CommunityResponse;
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

    public Boolean createBoard(Long userId, CommunityController.CreateBoardRequest request) {

        User user = userMapper.findById(userId).orElseThrow(UserNotFound::new);

        Community community = Community.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writer(user.getName())
                .build();

        return (communityMapper.createBoard(community) == 1);
    }

    public List<CommunityResponse> getBoardList(String type, String keyword, String sort) {

        List<CommunityResponse> collect = getSortList(type, keyword, sort);

        log.info("collect={}", collect);

        return collect;
    }

    public CommunityController.BoardResponse getBoard(Integer boardId) {
        Community findBoard = communityMapper.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        CommunityController.BoardResponse response = CommunityController.BoardResponse.builder()
                .title(findBoard.getTitle())
                .content(findBoard.getContent())
                .writer(findBoard.getWriter())
                .likeCount(findBoard.getLikeCount())
                .inserted(getFormatted(findBoard))
                .build();

        log.info("getBoard() response={}", response);

        return response;
    }

    public String getWriter(Long userId) {
        User user = userMapper.findById(userId).get();
        return user.getName();
    }

    public Integer updateLikeUp(Integer boardId) {
        communityMapper.updateLikeUp(boardId);
        Community findBoard = communityMapper.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return findBoard.getLikeCount();
    }

    public Integer updateLikeDown(Integer boardId) {
        communityMapper.updateLikeDown(boardId);
        Community findBoard = communityMapper.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return findBoard.getLikeCount();
    }

    private List<CommunityResponse> getSortList(String type, String keyword, String sort) {
        List<CommunityResponse> collect = null;

        if (sort.equals("id")) {
            collect = communityMapper.findAllDefault().stream()
                    .map(community -> CommunityResponse.builder()
                            .id(community.getId())
                            .title(community.getTitle())
                            .writer(community.getWriter())
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

    private static String getFormatted(Community findBoard) {
        return findBoard.getInserted().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
