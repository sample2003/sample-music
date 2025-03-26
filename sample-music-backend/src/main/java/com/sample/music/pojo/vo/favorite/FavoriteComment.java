package com.sample.music.pojo.vo.favorite;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sample.music.pojo.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class FavoriteComment extends Comment {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime favoriteTime;
}
