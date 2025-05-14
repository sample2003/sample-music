package com.sample.music.controller;

import com.sample.music.annotation.Public;
import com.sample.music.common.Result;
import com.sample.music.pojo.dto.ListenDTO;
import com.sample.music.pojo.vo.view.SongView;
import com.sample.music.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Public
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @PostMapping("/recordListen")
    public Result<?> recordListen(@RequestBody ListenDTO listenDTO) {
        // 判断收听信息是否存在
        if (listenDTO == null) {
            return Result.error();
        }

        statisticsService.recordListen(listenDTO);
        return Result.success();
    }

    @GetMapping("/topSongs")
    public Result<List<SongView>> getTopSongs() {
        List<SongView> songViews = statisticsService.getTopSongs();
        return Result.success(songViews);
    }

}
