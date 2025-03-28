package com.sample.music.controller;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.sample.music.common.Result;
import com.sample.music.pojo.dto.PageBean;
import com.sample.music.pojo.entity.Comment;
import com.sample.music.pojo.vo.CommentWithUser;
import com.sample.music.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    /**
     * 评论
     *
     * @param comment 评论
     */
    @PostMapping("insert/comment")
    public Result<String> insertSongComment(Comment comment) {
        boolean verify = SensitiveWordHelper.contains(String.valueOf(comment.getContent()));
        if (verify) {
            return Result.error("您的评论包含敏感词");
        }
        comment.setTargetType(comment.getTargetType());
        commentService.insertComment(comment);
        return Result.success();
    }

    /**
     * 查询评论
     *
     * @param targetId 目标id
     * @param pageNum 页数
     * @param pageSize 每页大小
     * @return Result<PageBean<CommentWithUser>>
     */
    @GetMapping("query/comment")
    public Result<PageBean<CommentWithUser>> queryCommentPaged(
            @RequestParam(required = false) Long targetId,
            @RequestParam(required = true) String targetType,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean<CommentWithUser> pb = commentService.queryCommentPaged(targetType, targetId, pageNum, pageSize);
        return Result.success(pb);
    }
}
