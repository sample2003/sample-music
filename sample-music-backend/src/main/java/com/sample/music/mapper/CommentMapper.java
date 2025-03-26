package com.sample.music.mapper;

import com.sample.music.pojo.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    void insertComment(Comment comment);

    void favoritesPlus(Long id);

    void favoriteSubtract(Long id);

    List<Comment> queryCommentPaged(String targetType, Long targetId);
}
