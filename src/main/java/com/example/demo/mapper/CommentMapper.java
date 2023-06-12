package com.example.demo.mapper;

import com.example.demo.entity.Comment;
import com.example.demo.request.comment.CreateCommentDto;
import com.example.demo.request.comment.DeleteCommentDto;
import com.example.demo.request.comment.UpdateCommentDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    Integer addComment(CreateCommentDto createCommentDto);

    List<Comment> findAllByBoardId(Integer boardId);

    Integer updateComment(UpdateCommentDto updateCommentDto);

    Integer deleteComment(DeleteCommentDto deleteCommentDto);
}
