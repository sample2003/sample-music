package com.sample.music.mapper;

import com.sample.music.pojo.entity.User;
import com.sample.music.pojo.dto.UserDTO;
import com.sample.music.pojo.vo.UserRegisterVO;
import com.sample.music.pojo.vo.UserUpdateVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    Long registerUser(User user);

    void addUserRole(Long roleId, Long userId);

    User findUserByUserName(String username);

    User findUserByEmail(String email);

    Long findUserRole(Long userId);

    String findRoleName(Long roleId);

    void updateUser(UserUpdateVO user, Long userId);

    void updatePwd(String newPassword, String email);

    UserDTO findUserById(Long id);

    void cancelUser(Long userId);

    void changeStatus(Long userId, int status);

    Integer selectUserExp(Long userId);

    void updateUserExp(Integer exp, Long userId);
}
