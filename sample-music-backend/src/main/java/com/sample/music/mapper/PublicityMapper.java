package com.sample.music.mapper;

import com.sample.music.pojo.entity.Publicity;
import com.sample.music.pojo.vo.view.PublicityView;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PublicityMapper {

    void insertPublicity(Publicity publicity);

    List<PublicityView> PagedQuery(String publicityType);

    List<PublicityView> conditionAndPagedQuery(String condition, String publicityType);

    PublicityView query(Long id);
}
