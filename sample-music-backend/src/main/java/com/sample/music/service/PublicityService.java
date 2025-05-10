package com.sample.music.service;

import com.github.pagehelper.PageHelper;
import com.sample.music.mapper.PublicityMapper;
import com.sample.music.pojo.dto.PageBean;
import com.sample.music.pojo.entity.Publicity;
import com.sample.music.pojo.vo.view.PublicityView;
import com.sample.music.utils.ThreadLocalUtil;
import com.sample.music.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PublicityService {
    private final PublicityMapper publicityMapper;
    private final FileManageService fileManageService;

    public void insertPublicity(Publicity publicity) {
        if(publicity.getImageFiles() != null) {
            String avatarUrl = fileManageService.uploadFile(publicity.getImageFiles(), "image");
            publicity.setCover(avatarUrl);
        }else {
            publicity.setCover(null);
        }
        Long userId = UserContext.getUser();
        publicity.setUserId(userId);
        publicity.setViewers(0);
        publicityMapper.insertPublicity(publicity);
    }

    public PageBean<PublicityView> conditionAndPagedQuery(String condition, String publicityType, Integer pageNum, Integer pageSize) {
        PageBean<PublicityView> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<PublicityView> publicityViews;// 直接使用分页结果的size作为总数
        if (condition == null || condition.isEmpty()) {
            // 没有条件，执行分页查询
            publicityViews = publicityMapper.PagedQuery(publicityType);
        } else {
            // 有条件，执行条件查询
            publicityViews = publicityMapper.conditionAndPagedQuery(condition, publicityType);
        }
        pb.setTotal(publicityViews.size()); // 直接使用分页结果的size作为总数
        pb.setItems(publicityViews);
        return pb;
    }

    public PublicityView query(Long id) {
        PublicityView query = publicityMapper.query(id);
        return query;
    }
}
