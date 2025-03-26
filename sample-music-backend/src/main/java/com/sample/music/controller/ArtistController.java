package com.sample.music.controller;

import com.sample.music.pojo.dto.PageBean;
import com.sample.music.pojo.entity.Artist;
import com.sample.music.common.Result;
import com.sample.music.service.ArtistService;
import com.sample.music.service.FileManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Transactional
@RestController
@RequestMapping("/api/artist")
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;
    private final FileManageService fileManageService;

    /**
     * 上传艺术家
     *
     * @param artist 艺术家信息
     */
    @PostMapping("insert")
    public Result<String> insertArtist(Artist artist) {
        artistService.insertArtistByOne(artist);
        return Result.success();
    }

    /**
     * 根据id删除艺术家信息
     *
     * @param id 艺术家id
     */
    @DeleteMapping("delete/{id}")
    public Result<?> deleteArtistById(@PathVariable("id") Long id) {
        if (artistService.isExist(id)) {
            Artist artist = artistService.selectArtistById(id);
            fileManageService.delete(artist.getAvatar());
            artistService.deleteArtistById(id);
            return Result.success();
        }
        return Result.error("艺术家不存在");
    }

    /**
     * 更新艺术家信息
     *
     * @param artist
     * @return
     */
    @PutMapping("update")
    public Result<Artist> updateArtistById(Artist artist) {
        if (artistService.isExist(artist.getId())) {
            String avatarUrl = fileManageService.uploadFile(artist.getImageFiles(), "cover");
            artist.setAvatar(avatarUrl);
            artistService.updateArtistById(artist);
            return Result.success(artist);
        }
        return Result.error("艺术家不存在");
    }

    /**
     * 根据id查询艺术家
     * @param id
     * @return
     */
    @GetMapping("select/{id}")
    public Result<Artist> selectArtistById(@PathVariable("id") Long id) {
        Artist artist = artistService.selectArtistById(id);
        if (artist == null) {
            return Result.error("艺术家不存在");
        }
        return Result.success(artist);
    }

    @GetMapping("select")
    public Result<List<Artist>> selectArtistByAll() {
        List<Artist> artists = artistService.selectArtistByAll();
        return Result.success(artists);
    }

    /**
     * 条件分页查询艺术家
     *
     * @param condition 条件
     * @param pageNum   分页参数，页码
     * @param pageSize  分页参数，每页大小
     * @return Result<PageBean<Artist>>
     */
    @GetMapping("query/conditionAndPaged")
    public Result<PageBean<Artist>> conditionAndPaged(
            @RequestParam(required = false) String condition,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean<Artist> pb = artistService.conditionAndPagedQuery(condition, pageNum, pageSize);
        return Result.success(pb);
    }
}
