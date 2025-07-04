package com.sample.music.mapper;

import com.sample.music.pojo.entity.RequestLog;
import com.sample.music.pojo.entity.User;
import com.sample.music.pojo.dto.UserDTO;
import com.sample.music.pojo.vo.UserUpdateVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    Long registerUser(User user);

    void insertLogger(RequestLog requestLog);

    void addUserConfig(Long userIdLong, Integer role);

    User findUserByUserName(String username);

    User findUserByEmail(String email);

    Integer findUserRole(Long userId);

    void updateUser(UserUpdateVO user, Long userId);

    void updatePwd(String newPassword, String email);

    UserDTO findUserById(Long id);

    void cancelUser(Long userId);

    void changeStatus(Long userId, int status);

    Integer selectUserExp(Long userId);

    void updateUserExp(Integer exp, Long userId);
}
