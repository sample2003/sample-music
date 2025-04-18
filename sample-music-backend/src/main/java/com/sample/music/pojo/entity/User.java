package com.sample.music.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sample.music.annotation.Status;
import com.sample.music.common.FilesType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
public class User {
    /**
     * 用户id
     */
    @NotNull
    private Long id;

    /**
     * 用户名
     */
    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}")
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    @Email
    private String email;

    /**
     * 性别
     */
    private String sex;

    /**
     * 身份证号
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 关注数
     */
    private Integer likes;

    /**
     * 经验
     */
    private Integer exp;

    /**
     * 简介
     */
    private String description;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateTime;

}