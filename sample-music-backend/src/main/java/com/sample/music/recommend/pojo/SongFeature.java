package com.sample.music.recommend.pojo;

import lombok.Data;

// 歌曲内容特征实体
@Data
public class SongFeature {
    private String id;
    private double[] mfcc;       // MFCC特征
    private double tempo;        // 节奏（BPM）
    private String style;        // 风格（Style）
}

