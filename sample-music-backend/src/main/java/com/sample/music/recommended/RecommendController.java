package com.sample.music.recommended;

import com.sample.music.common.Result;
import com.sample.music.recommended.pojo.SongFeature;
import com.sample.music.recommended.service.ContentBasedService;
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
