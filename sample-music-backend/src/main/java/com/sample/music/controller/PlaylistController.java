package com.sample.music.controller;

import com.sample.music.annotation.AdminOnly;
import com.sample.music.annotation.Public;
import com.sample.music.pojo.dto.PageBean;
import com.sample.music.pojo.dto.PlaylistUpload;
import com.sample.music.pojo.entity.Comment;
import com.sample.music.pojo.entity.Playlist;
import com.sample.music.common.Result;
import com.sample.music.pojo.vo.CommentWithUser;
import com.sample.music.pojo.vo.PlaylistWithSongs;
import com.sample.music.service.CommentService;
import com.sample.music.service.PlaylistService;
import com.sample.music.service.FileManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/playlist")
public class PlaylistController {

    private final PlaylistService playlistService;

    private final FileManageService fileManageService;

    private final CommentService commentService;

    /**
     * 创建歌单（用户）
     *
     * @param playlistUpload 歌单信息
     */
    @PostMapping("insert")
    public Result<Boolean> insertPlaylistByOne(PlaylistUpload playlistUpload) {
        playlistService.insertPlaylist(playlistUpload);
        return Result.success(true);
    }

    /**
     * 删除歌单（用户）
     *
     * @param id 歌单id
     */
    @DeleteMapping("delete/{id}")
    public Result<String> deletePlayListById(@PathVariable("id") Long id) {
        if (playlistService.isExist(id)) {
            playlistService.deletePlayListById(id);
            return Result.success();
        }
        return Result.error("歌单不存在");
    }

    /**
     * 修改歌单（用户）
     *
     * @param playlist 歌单信息与图片文件
     * @return Result<PlayList>
     */
    @PutMapping("update")
    public Result<Playlist> updatePlaylistById(Playlist playlist) {
        if (playlistService.isExist(playlist.getId())) {
            if(playlist.getImageFiles() != null) {
                String coverUrl = fileManageService.uploadFile(playlist.getImageFiles(), "cover");
                playlist.setCover(coverUrl);
            }
            playlistService.updatePlayListById(playlist);
            return Result.success(playlist);
        }
        return Result.error("歌单不存在");
    }

    /**
     * 修改歌单封面
     *
     * @param id 歌单id
     * @param imageFiles 图片文件
     * @return Result<PlayList>
     */
    @PutMapping("update/cover/{id}")
    public Result<String> updatePlaylistCover(@PathVariable("id") Long id, MultipartFile imageFiles) {
        if (playlistService.isExist(id)) {
            String coverUrl = fileManageService.uploadFile(imageFiles, "cover");
            playlistService.updatePlaylistCover(id, coverUrl);
            return Result.success("更新歌单封面成功");
        }
        return Result.error("歌单不存在");
    }

    /**
     * 查询所有歌单（管理员）
     *
     * @return Result<List<PlayList>>
     */
    @AdminOnly
    @GetMapping("query")
    public Result<List<Playlist>> queryPlayListByAll() {
        List<Playlist> playlists = playlistService.queryPlayListByAll();
        return Result.success(playlists);
    }

    /**
     * 查询一个歌单（游客）
     *
     * @param id 歌单id
     * @return Result<PlayList>
     */
    /*@GetMapping("select/{id}")
    public Result<Playlist> selectPlayListById(@PathVariable("id") Long id) {
        Playlist playList = playlistService.selectPlayListById(id);
        if (playList == null) {
            return Result.error("歌单不存在");
        }
        return Result.success(playList);
    }*/

    /**
     * 查询一个歌单和相关歌曲信息（游客）
     *
     * @return Result<PlaylistWithSongs>
     */
    @GetMapping("select/{id}")
    public Result<PlaylistWithSongs> queryPlaylistWithSongs(@PathVariable("id") Long id) {
        PlaylistWithSongs playlistWithSongs = playlistService.queryPlaylistWithSongs(id);
        return Result.success(playlistWithSongs);
    }

    /**
     * 查询歌单（游客）
     *
     * @param condition 条件
     * @param pageNum   分页参数，页码
     * @param pageSize  分页参数，每页大小
     * @return Result<PageBean < PlayList>>
     */
    @Public
    @GetMapping("select")
    public Result<PageBean<Playlist>> selectPlaylists(
            @RequestParam(required = false) String condition,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean<Playlist> pb = playlistService.conditionAndPagedQuery(condition, pageNum, pageSize);
        return Result.success(pb);
    }

    /**
     * 分页与条件查询歌单（用户）
     *
     * @return Result<PageBean<PlaylistWithSongs>>
     */
    @GetMapping("query/UserPagedQuery")
    public Result<PageBean<PlaylistWithSongs>> UserPagedQuery(
            @RequestParam(defaultValue = "true") boolean isPublic,
            @RequestParam(required = false) String condition,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize
    ) {
        PageBean<PlaylistWithSongs> pb = playlistService.UserPagedQuery(isPublic, condition, pageNum, pageSize);
        return Result.success(pb);
    }

    /**
     * 查询歌单评论（用户）
     *
     * @param targetId 歌单id
     * @param pageNum 页数
     * @param pageSize 每页大小
     * @return Result<PageBean<CommentWithUser>>
     */
    @GetMapping("query/comment")
    public Result<PageBean<CommentWithUser>> queryCommentPaged(
            @RequestParam(required = false) Long targetId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "100") Integer pageSize) {
        String targetType = "playlist";
        PageBean<CommentWithUser> pb = commentService.queryCommentPaged(targetType, targetId, pageNum, pageSize);
        return Result.success(pb);
    }

    /**
     * 评论歌单（用户）
     *
     * @param comment 评论
     */
    @PostMapping("insert/comment")
    public Result<String> insertPlaylistComment(Comment comment) {
        comment.setTargetType("playlist");
        commentService.insertComment(comment);
        return Result.success();
    }

    /**
     * 收听数+1（游客）
     *
     * @param id 歌曲id
     */
    @Public
    @Transactional
    @PutMapping("listenersPlusOne/{id}")
    public Result<String> listenersPlusOne(@PathVariable Long id) {
        playlistService.listenersPlusOne(id);
        return Result.success();
    }
}

