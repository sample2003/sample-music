package com.sample.music.controller;

import com.sample.music.annotation.Public;
import com.sample.music.pojo.dto.PageBean;
import com.sample.music.pojo.entity.Album;
import com.sample.music.common.Result;
import com.sample.music.pojo.entity.Comment;
import com.sample.music.pojo.vo.AlbumWithSongs;
import com.sample.music.pojo.vo.CommentWithUser;
import com.sample.music.pojo.vo.upload.AlbumUpload;
import com.sample.music.pojo.vo.view.AlbumView;
import com.sample.music.service.AlbumService;
import com.sample.music.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/album")
public class AlbumController {
    private final AlbumService albumService;

    private final CommentService commentService;

    /**
     * 上传专辑
     *
     * @param albumUpload 专辑信息
     * @return Album
     */
    @PostMapping("insert")
    public Result<Album> insertAlbum(AlbumUpload albumUpload) {
        albumService.insertAlbumByOne(albumUpload);
        return Result.success();
    }

    /**
     * 删除专辑
     *
     * @param id 专辑id
     */
    @DeleteMapping("delete/{id}")
    public Result<String> deleteAlbumById(@PathVariable("id") Long id) {
        albumService.deleteAlbumById(id);
        return Result.success();
    }

    /**
     * 更新专辑信息
     *
     * @param album 专辑信息
     * @return Album
     */
    @PutMapping("update")
    public Result<Album> updatePlayListById(Album album) {
        albumService.updateAlbumById(album);
        return Result.success(album);
    }

    /**
     * 根据id查询专辑
     *
     * @param id 专辑id
     * @return Result<Album>
     */
    @Public
    @GetMapping("select/{id}")
    public Result<AlbumView> selectAlbumById(@PathVariable("id") Long id) {
        AlbumView album = albumService.selectAlbumById(id);
        if (album == null) {
            return Result.error("专辑不存在");
        }
        return Result.success(album);
    }


    /**
     * 查询一个专辑信息和相关歌曲
     *
     * @return Result<AlbumWithSongs>
     */
    @Public
    @GetMapping("select/albumWithSongs/{id}")
    public Result<AlbumWithSongs> selectAlbumWithSongs(@PathVariable("id") Long id) {
        AlbumWithSongs albumWithSongs = albumService.selectAlbumWithSongs(id);
        return Result.success(albumWithSongs);
    }

    /**
     * 条件分页查询专辑
     *
     * @param condition 条件
     * @param pageNum   分页参数，页码
     * @param pageSize  分页参数，每页大小
     * @return Result<PageBean < Album>>
     */
    @Public
    @GetMapping("query/conditionAndPaged")
    public Result<PageBean<AlbumView>> conditionAndPagedQuery(
            @RequestParam(required = false) String condition,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean<AlbumView> pb = albumService.conditionAndPagedQuery(condition, pageNum, pageSize);
        return Result.success(pb);
    }

    /**
     * 评论专辑
     *
     * @param comment 评论
     */
    @PostMapping("insert/comment")
    public Result<String> insertAlbumComment(Comment comment) {
        comment.setTargetType("album");
        commentService.insertComment(comment);
        return Result.success();
    }

    /**
     * 查询专辑评论
     * @param targetId 专辑id
     * @param pageNum 页数
     * @param pageSize 每页大小
     * @return Result<PageBean<CommentWithUser>>
     */
    @Public
    @GetMapping("query/comment")
    public Result<PageBean<CommentWithUser>> queryCommentPaged(
            @RequestParam(required = false) Long targetId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        String targetType = "album";
        PageBean<CommentWithUser> pb = commentService.queryCommentPaged(targetType, targetId, pageNum, pageSize);
        return Result.success(pb);
    }

    /**
     * 收听数+1
     *
     * @param id 歌曲id
     */
    @Public
    @Transactional
    @PutMapping("listenersPlusOne/{id}")
    public Result<String> listenersPlusOne(@PathVariable Long id) {
        albumService.listenersPlusOne(id);
        return Result.success();
    }
}
