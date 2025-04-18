package com.sample.music.pojo.entity;

import com.sample.music.annotation.Status;
import lombok.Data;

@Data
public class UserConfig {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户身份
     */
    private Integer role;

    /**
     * 状态 0:登录，1:启用，2:禁用
     */
    private Integer status;

    /**
     * 是否被删除 0:没有
     */
    @Status
    private Integer deleted;
}
