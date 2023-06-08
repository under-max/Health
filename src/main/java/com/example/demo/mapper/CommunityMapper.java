package com.example.demo.mapper;

import com.example.demo.entity.Community;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommunityMapper {

    Integer createBoard(Community community);

    Optional<Community> findById(Integer boardId);

    List<Community> findAllDefault();

    List<Community> findAllSortByLike();

    List<Community> findAllSortByComment();

    List<Community> findAllSortByView();

    void updateLikeUp(Integer boardId);

    void updateLikeDown(Integer boardId);
}
