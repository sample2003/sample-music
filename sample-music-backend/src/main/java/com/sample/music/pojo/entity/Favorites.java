package com.sample.music.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Favorites {
    private Long id;
    private Long userId;
    private String targetType;
    private Long targetId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime favoriteTime;
}
