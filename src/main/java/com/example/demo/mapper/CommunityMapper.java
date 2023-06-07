package com.example.demo.mapper;

import com.example.demo.entity.Community;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityMapper {

    Integer createBoard(Community community);

    List<Community> findAllSortById();

    List<Community> findAllSortByLike();

    List<Community> findAllSortByComment();

    List<Community> findAllSortByView();

    Community findById(Integer boardId);

}
