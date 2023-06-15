package com.example.demo.mapper;

import com.example.demo.entity.Community;
import com.example.demo.response.board.CommunityResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommunityMapper {

    Integer createBoard(Community community);

    Optional<Community> findById(Integer boardId);

    Integer countAll(String type, String keyword);

    List<CommunityResponseDto> findAll(String type, String keyword, String sort, Integer startIndex, Integer rowPage);

    void updateLikeUp(Integer boardId);

    void updateLikeDown(Integer boardId);

    void viewCount(Integer boardId);

    Integer updateBoard(@Param("boardId") Integer boardId, @Param("writer") String writer, @Param("title") String title, @Param("content") String content);

    Integer deleteBoard(@Param("boardId") Integer boardId, @Param("writer") String writer);

}
