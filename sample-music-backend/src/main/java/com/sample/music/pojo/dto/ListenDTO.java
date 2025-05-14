package com.sample.music.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ListenDTO {
    private Long id;

    private Long userId;

    private Long songId;

    private String duration;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String listenTime;
}
