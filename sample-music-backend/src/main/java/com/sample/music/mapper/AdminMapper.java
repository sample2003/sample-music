package com.sample.music.mapper;

import com.sample.music.pojo.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {

    List<UserDTO> selectUsersByPage(Integer status);

    Long findUserRoleId(Long userId);
    String findUserRoleName(Long roleId);
    void BarreOrFreedUser(Long id, Integer status);
}
