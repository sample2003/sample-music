package com.sample.music.mapper;

import com.sample.music.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {

    int countAll();

    int countByCondition(String condition, String params);

    List<User> PagedQuery();

    List<User> conditionAndPagedQuery(String condition, String params);

    Long findUserRoleId(Long userId);

    String findUserRoleName(Long roleId);

    void BarreOrFreedUser(Long id, int status);
}
