package com.example.demo.mapper;

import com.example.demo.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    Integer addComment(@Param("boardId") Integer boardId, @Param("content") String content, @Param("writer") String writer);

    List<Comment> findAllByBoardId(Integer boardId);

    void updateComment(Integer boardId, String writer);

    void deleteComment(Integer boardId, String writer);
}
