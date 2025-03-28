package com.sample.music.controller;

import com.sample.music.annotation.AdminOnly;
import com.sample.music.common.Result;
import com.sample.music.exception.BusinessException;
import com.sample.music.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AdminOnly
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * 用户封禁
     */
    public Result<String> BarreOrFreedUser(Long userId, int status) {
        try {
            adminService.BarreOrFreedUser(userId, status);
        }catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
        return Result.success("该账户封禁成功");
    }

    /**
     * 查询用户状态
     */

    /**
     * 分页查询用户信息
     */
}
