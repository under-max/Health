package com.example.demo.mapper;

import com.example.demo.entity.Community;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommunityMapper {

    Integer createBoard(Community community);

    Optional<Community> findById(Integer boardId);

    List<Community> findAll(String sort);

    List<Community> findAllSortByLike();

    List<Community> findAllSortByComment();

    List<Community> findAllSortByView();

    void updateLikeUp(Integer boardId);

    void updateLikeDown(Integer boardId);

    void viewCount(Integer boardId);

    Integer deleteBoard(@Param("boardId") Integer boardId, @Param("writer") String writer);
}
