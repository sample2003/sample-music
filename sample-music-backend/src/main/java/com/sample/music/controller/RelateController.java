package com.sample.music.controller;

import com.sample.music.pojo.entity.Song;
import com.sample.music.common.Result;
import com.sample.music.service.RelateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/relate")
@RequiredArgsConstructor
public class RelateController {

    private final RelateService relateService;

    /**
     * 增加歌曲到歌单
     *
     * @param ids         歌曲id
     * @param playlistId 歌单id
     * @return Result
     */
    @PostMapping("addSongsToPlaylist")
    public Result<String> addSongsToPlayList(@RequestParam Long playlistId, @RequestBody Long[] ids) {
        for (Long id : ids) {
            relateService.addSongsToPlaylist(playlistId, id);
        }
        return Result.success();
    }

    /**
     * 从歌单移除歌曲
     *
     * @param ids         歌曲id
     * @param playlistId 歌单id
     * @return Result
     */
    @DeleteMapping("deleteSongsByPlaylist")
    public Result<String> deleteSongsByPlaylist(@RequestParam Long playlistId, @RequestBody Long[] ids) {
        for (Long id : ids) {
            relateService.deleteSongsByPlaylist(playlistId, id);
        }
        return Result.success();
    }

    /**
     * 从歌单查询歌曲
     *
     * @param id 歌单id
     * @return Result<List < Song>>
     */
    @GetMapping("selectSongsAllByPlaylist/{id}")
    public Result<List<Song>> selectSongsAllByPlaylist(@PathVariable("id") Long id) {
        List<Song> song = relateService.selectSongsAllByPlaylist(id);
        return Result.success(song);
    }

    /**
     * 查询专辑里的所有歌曲
     *
     * @param id 专辑id
     * @return Result<List < Song>>
     */
    @GetMapping("selectSongsByAlbum/{id}")
    public Result<List<Song>> selectSongsAllByAlbum(@PathVariable("id") Long id) {
        List<Song> song = relateService.selectSongsAllByAlbum(id);
        return Result.success(song);
    }

}
