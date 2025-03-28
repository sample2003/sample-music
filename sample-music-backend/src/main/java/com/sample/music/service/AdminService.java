package com.sample.music.service;

import com.github.pagehelper.PageHelper;
import com.sample.music.constant.HttpStatusCode;
import com.sample.music.constant.SortType;
import com.sample.music.exception.BusinessException;
import com.sample.music.mapper.AdminMapper;
import com.sample.music.pojo.dto.PageBean;
import com.sample.music.pojo.entity.User;
import com.sample.music.pojo.vo.view.SongView;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminMapper adminMapper;

    public void BarreOrFreedUser(Long userId, int status) {
        if (status == 1) {
            throw new BusinessException(HttpStatusCode.OK, "该账户已封禁");
        }
        adminMapper.BarreOrFreedUser(userId, status);
    }


    /**
     * 条件并分页查询用户
     *
     * @param condition 条件
     * @param params    条件字段
     * @param pageNum   分页参数，页码
     * @param pageSize  分页参数，每页大小
     * @param sortType  排序字段
     * @return PageBean<Song>
     */
    @Cacheable(
            value = "userPageCache",
            key = "{#condition, #params, #pageNum, #pageSize, #sortType?.name()}"
    )
    public PageBean<SongView> conditionAndPagedQuery(
            String condition,
            String params,
            Integer pageNum,
            Integer pageSize,
            SortType sortType)
    {
        // 先获取总数
        int total;
        if (condition == null || condition.isEmpty()) {
            // 没有条件，执行总数查询
            total = adminMapper.countAll();
        } else {
            // 有条件，执行条件总数查询
            total = adminMapper.countByCondition(condition, params);
        }
        PageBean<SongView> pb = new PageBean<>();
        // 设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        if (sortType != null) {
            PageHelper.orderBy(String.format("%s %s", sortType.getField(), sortType.getDirection()));
        }
        // 执行分页查询
        List<User> users;
        List<SongView> songViews = new ArrayList<>();
        if (condition == null || condition.isEmpty()) {
            // 没有条件，执行分页查询
            users = adminMapper.PagedQuery();
        } else {
            // 有条件，执行条件查询
            users = adminMapper.conditionAndPagedQuery(condition, params);
        }
        pb.setTotal(total); // 直接使用分页结果的size作为总数
        pb.setItems(songViews);
        return pb;
    }
}
