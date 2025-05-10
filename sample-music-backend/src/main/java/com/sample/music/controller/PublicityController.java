package com.sample.music.controller;

import com.sample.music.annotation.Public;
import com.sample.music.common.Result;
import com.sample.music.pojo.dto.PageBean;
import com.sample.music.pojo.entity.Publicity;
import com.sample.music.pojo.vo.view.PublicityView;
import com.sample.music.service.PublicityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/publicity")
public class PublicityController {
    public final PublicityService publicityService;

    /**
     * 发布公告
     *
     * @param publicity 公告信息
     */
    @PostMapping("insert")
    public Result<String> insertPublicity(Publicity publicity) {
        publicityService.insertPublicity(publicity);
        return Result.success();
    }

    /**
     * 查询公告
     *
     * @param condition 条件
     * @param pageNum   分页参数，页码
     * @param pageSize  分页参数，每页大小
     * @return Result<PageBean < Album>>
     */
    @Public
    @GetMapping("query/conditionAndPaged")
    public Result<PageBean<PublicityView>> conditionAndPagedQuery(
            @RequestParam(required = false) String condition,
            @RequestParam(defaultValue = "notice" , required = true) String publicityType,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean<PublicityView> pb = publicityService.conditionAndPagedQuery(condition, publicityType, pageNum, pageSize);
        return Result.success(pb);
    }

    /**
     * 一个查询公告
     *
     * @return Result<PageBean < Album>>
     */
    @Public
    @GetMapping("query/{id}")
    public Result<PublicityView> query(@PathVariable Long id) {
        PublicityView pb = publicityService.query(id);
        return Result.success(pb);
    }
}
