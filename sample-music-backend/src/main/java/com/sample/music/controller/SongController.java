package com.sample.music.controller;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.google.gson.Gson;
import com.sample.music.annotation.AdminOnly;
import com.sample.music.annotation.Public;
import com.sample.music.common.Result;
import com.sample.music.constant.SortType;
import com.sample.music.constant.TargetType;
import com.sample.music.exception.BusinessException;
import com.sample.music.pojo.dto.PageBean;
import com.sample.music.pojo.dto.SongUpload;
import com.sample.music.pojo.entity.Comment;
import com.sample.music.pojo.entity.Song;
import com.sample.music.pojo.vo.CommentWithUser;
import com.sample.music.pojo.vo.view.SongView;
import com.sample.music.service.CommentService;
import com.sample.music.service.FileManageService;
import com.sample.music.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Target;
import java.util.List;

import static com.sample.music.constant.HttpStatusCode.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/song")
public class SongController {

    private final SongService songService;

    private final FileManageService fileManageService;

    private final static Gson GSON = new Gson();

    private final CommentService commentService;

    /**
     * 新增歌曲，解析上传（管理员）
     *
     * @param song 歌曲信息
     * @return Result<Song>
     */
    /*@AdminOnly
    @PostMapping("insertAndParse")
    public Result<Song> insertAndParseSong(Song song) {
        if (song == null) return Result.error(NOT_FOUND, "歌曲为空", null);
        return Result.success(songService.insertAndParseSong(song));
    }*/

    /**
     * 新增歌曲（管理员）
     *
     * @param songUpload 歌曲信息
     * @return Result<Song>
     */
    @AdminOnly
    @PostMapping("insert")
    public Result<Boolean> insertSong(SongUpload songUpload) {
        boolean result = songService.insertSong(songUpload);
        return Result.success(result);
    }

    /**
     * 删除歌曲（管理员）
     *
     * @param songId 歌曲Id
     * @return Result<Song>
     */
    @AdminOnly
    @DeleteMapping("delete/{songId}")
    public Result<Boolean> deleteSongById(@PathVariable Long songId) {
        boolean result = songService.deleteAndRemoveSong(songId);
        return Result.success(result);
    }

    /**
     * 修改歌曲（管理员）
     *
     * @param songUpdate 更新歌曲信息
     * @return Result<Song>
     */
    @AdminOnly
    @PutMapping("update")
    public Result<Song> updateSong(SongUpload songUpdate) {
        if (songUpdate == null) return Result.error("歌曲为空");
        // 上传歌词文件到OSSd
        String lyricUrl = fileManageService.uploadFile(songUpdate.getLyric(), "lyric");
        // 上传图片文件到OSS
        String coverUrl = fileManageService.uploadFile(songUpdate.getImageFiles(), "cover");
        // 上传无损歌曲文件到OSS
        String flacSongUrl = fileManageService.uploadFile(songUpdate.getFlacUrl(), "song");
        // 上传歌曲文件到OSS
        String mp3SongUrl = fileManageService.uploadFile(songUpdate.getMp3Url(), "song");

        if (!lyricUrl.equals("error") && !coverUrl.equals("error") && !mp3SongUrl.equals("error") && !flacSongUrl.equals("error")) {
            Song song = new Song();
            BeanUtils.copyProperties(songUpdate, song);
            song.setLyric(lyricUrl);
            song.setCover(coverUrl);
            song.setFlacUrl(flacSongUrl);
            song.setMp3Url(mp3SongUrl);
            List<String> tags = songUpdate.getTags();
            song.setTags(GSON.toJson(tags));
            return Result.success(songService.updateSong(song));
        }
        throw new BusinessException(INTERNAL_SERVER_ERROR, "歌曲更新数据失败");
    }

    /**
     * 条件分页查询歌曲（游客）
     *
     * @param condition 条件
     * @param pageNum   分页参数，页码
     * @param pageSize  分页参数，每页大小
     * @return Result<PageBean < Song>>
     */
    @Public
    @GetMapping("query")
    public Result<PageBean<SongView>> query(
            @RequestParam(required = false) String condition,
            @RequestParam(defaultValue = "ALL") TargetType params,
            @RequestParam(defaultValue = "LISTENERS_DESC") SortType sortType,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean<SongView> pb = songService.query(condition, params, pageNum, pageSize, sortType);
        return Result.success(pb);
    }

    /**
     * 条件分页查询歌曲（管理员）
     *
     * @param condition 条件
     * @param pageNum   分页参数，页码
     * @param pageSize  分页参数，每页大小
     * @return Result<PageBean < Song>>
     */
    @AdminOnly
    @GetMapping("select")
    public Result<PageBean<Song>> select(
            @RequestParam(required = false) String condition,
            @RequestParam(defaultValue = "ALL") TargetType params,
            @RequestParam(defaultValue = "LISTENERS_DESC") SortType sortType,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean<Song> pb = songService.select(condition, params, pageNum, pageSize, sortType);
        return Result.success(pb);
    }

    /**
     * 根据id查询一首歌曲
     *
     * @param songId 歌曲Id
     * @return Song 歌曲
     */
    @GetMapping("query/{songId}")
    public Result<SongView> queryBySongId(@PathVariable Long songId) {
        SongView song = songService.querySongById(songId);
        if (song == null) return Result.error("未找到歌曲");
        return Result.success(song);
    }

    /**
     * 查询歌曲评论
     *
     * @param targetId 歌曲id
     * @param pageNum 页数
     * @param pageSize 每页大小
     * @return Result<PageBean<CommentWithUser>>
     */
    @GetMapping("query/comment")
    public Result<PageBean<CommentWithUser>> queryCommentPaged(
            @RequestParam(required = false) Long targetId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        String targetType = "song";
        PageBean<CommentWithUser> pb = commentService.queryCommentPaged(targetType, targetId, pageNum, pageSize);
        return Result.success(pb);
    }

    /**
     * 收听数+1
     *
     * @param id 歌曲id
     */
    @Transactional
    @PutMapping("listenersPlusOne/{id}")
    public Result<String> listenersPlusOne(@PathVariable Long id) {
        songService.listenersPlusOne(id);
        return Result.success();
    }

    /**
     * 评论歌曲
     *
     * @param comment 评论
     */
    @PostMapping("insert/comment")
    public Result<String> insertSongComment(Comment comment) {
        boolean verify = SensitiveWordHelper.contains(String.valueOf(comment));
        comment.setTargetType("song");
        commentService.insertComment(comment);
        return Result.success();
    }
}
