package com.sample.music.recommend;

import com.sample.music.common.Result;
import com.sample.music.recommend.pojo.SongFeature;
import com.sample.music.recommend.service.ContentBasedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class RecommendController {
    private final ContentBasedService contentBasedService;

    @GetMapping("song")
    public Result<SongFeature> getRecommendSongs() {
        SongFeature ss = contentBasedService.ss(1L);
        return Result.success(ss);
    }
}
