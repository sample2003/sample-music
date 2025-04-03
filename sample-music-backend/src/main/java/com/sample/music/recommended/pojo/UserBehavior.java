package com.sample.music.recommended.pojo;

import lombok.Data;

// 用户行为记录实体
@Data
public class UserBehavior {
    private String userId;
    private String songId;
    private int playCount;      // 播放次数
    private boolean liked;      // 是否收藏
}