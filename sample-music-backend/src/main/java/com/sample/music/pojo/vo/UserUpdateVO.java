package com.sample.music.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserUpdateVO {
    // 值不能为null
    @NotNull
    private Long id;
    // 值不能为null，并且非空
    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}")
    private String nickname;
    // 满足邮箱格式
    @Email
    private String email;
    private String phone;
    private String avatar;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateTime;
}
