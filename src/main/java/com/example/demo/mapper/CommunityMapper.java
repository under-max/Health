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

    Integer countAll(@Param("type") String type, @Param("keyword") String keyword);

    List<CommunityResponseDto> findAll(@Param("type") String type, @Param("keyword") String keyword, @Param("sort") String sort, @Param("startIndex") Integer startIndex, @Param("rowPage") Integer rowPage);

    void updateLikeUp(Integer boardId);

    void updateLikeDown(Integer boardId);

    void viewCountPlus(Integer boardId);

    Integer updateBoard(@Param("boardId") Integer boardId, @Param("writer") String writer, @Param("title") String title, @Param("content") String content);

    Integer deleteBoard(@Param("boardId") Integer boardId, @Param("writer") String writer);

}
