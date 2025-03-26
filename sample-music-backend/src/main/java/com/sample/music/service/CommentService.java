package com.sample.music.service;

import com.github.pagehelper.PageHelper;
import com.sample.music.mapper.CommentMapper;
import com.sample.music.mapper.UserMapper;
import com.sample.music.pojo.dto.PageBean;
import com.sample.music.pojo.dto.UserDTO;
import com.sample.music.pojo.entity.Comment;
import com.sample.music.pojo.vo.CommentWithUser;
import com.sample.music.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;

    private final UserMapper userMapper;

    /**
     * 新增评论
     *
     * @param comment 评论内容
     */
    public void insertComment(Comment comment) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        long userId = id.longValue();
        comment.setUserId(userId);
        comment.setLikes(0);
        commentMapper.insertComment(comment);
    }

    /**
     * 分页查询相关评论
     * @param targetType 目标类型
     * @param pageNum 页数
     * @param pageSize 每页大小
     * @return PageBean<Comment>
     */
    public PageBean<CommentWithUser> queryCommentPaged(String targetType, Long targetId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> comments = commentMapper.queryCommentPaged(targetType, targetId);
        List<CommentWithUser> commentWithUsers = new ArrayList<CommentWithUser>();
        // 查询用户信息并更新评论列表
        comments.forEach(comment -> {
            UserDTO user = userMapper.findUserById(comment.getUserId());
            if (user != null) {
                CommentWithUser commentWithUser = new CommentWithUser();
                BeanUtils.copyProperties(comment, commentWithUser);
                commentWithUser.setUserAvatar(user.getAvatar());
                commentWithUser.setUsername(user.getUsername());
                commentWithUsers.add(commentWithUser);
            }
        });
        PageBean<CommentWithUser> pb = new PageBean<>();

        pb.setTotal(commentWithUsers.size());
        pb.setItems(commentWithUsers);

        return pb;
    }
}
