package com.sample.music.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {
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
     * 个人简介
     */
    private String description;
    /**
     * 关注数
     */
    private Integer likes;
    /**
     * 状态 0:登录，1:启用，2:禁用
     */
    private Integer status;
    /**
     * 经验值
     */
    private Integer exp;
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

    private String role;
}
