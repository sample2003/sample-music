package com.sample.music.service;

import com.sample.music.exception.BusinessException;
import com.sample.music.pojo.dto.EmailVerify;
import com.sample.music.pojo.entity.User;
import com.sample.music.pojo.dto.UserDTO;
import com.sample.music.pojo.vo.UserRegisterVO;
import com.sample.music.pojo.vo.UserUpdateVO;
import com.sample.music.mapper.UserMapper;
import com.sample.music.utils.Md5Util;
import com.sample.music.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    private final EmailService emailService;

    private final StringRedisTemplate stringRedisTemplate;

    public User findUserByName(String username) {
        return userMapper.findUserByUserName(username);
    }

    public User findUserByEmail(String email) {
        return userMapper.findUserByEmail(email);
    }

    public User findUserByNameOrEmail(String text) {
        User user = userMapper.findUserByUserName(text);
        if(user == null){
            user = userMapper.findUserByEmail(text);
        }
        return user;
    }

    public UserDTO findUserById(Long id) {
        UserDTO user = userMapper.findUserById(id);
        Long roleId = userMapper.findUserRole(id);
        String roleName = userMapper.findRoleName(roleId);
        user.setRole(roleName);
        return user;
    }

    @Transactional
    public Long registerUser(UserRegisterVO user) {
        String email = user.getEmail();
        String password = user.getPassword();
        String verificationCode = user.getVerificationCode();
        String invitationCode = user.getInvitationCode();
        // 验证验证码或邀请码是否存在
        boolean isValidCode = verificationCode != null && emailService.validateVerifyCode(email, verificationCode);
        boolean hasInvitationCode = invitationCode.equals("cqt123");

        // 如果没有有效的验证码或邀请码，抛出异常
        if (!isValidCode && !hasInvitationCode) {
            throw new BusinessException(401, "注册失败");
        }

        // 如果有有效的验证码，删除验证码
        if (isValidCode) {
            emailService.deleteVerifyCode(email);
        }

        // 设置密码并注册用户
        String passwordToMd5 = Md5Util.getMD5String(password);
        User userInfo = new User();
        BeanUtils.copyProperties(user, userInfo);
        userInfo.setLikes(0);
        userInfo.setPassword(passwordToMd5);
        Long id = userMapper.registerUser(userInfo);

        // 添加用户角色，默认普通用户
        Long roleId = 2L;
        Long userId = userInfo.getId();
        userMapper.addUserRole(roleId, userId);

        return id;
    }

    public void updateUser(UserUpdateVO user) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        Long userId = id.longValue();
        if (user.getId().equals(userId)) {
            userMapper.updateUser(user, userId);
        }else {
            throw new BusinessException(404, "该用户不存在");
        }
    }
    
    public void updatePwd(String newPassword, String email) {
        String passwordToMd5 = Md5Util.getMD5String(newPassword);
        userMapper.updatePwd(passwordToMd5, email);
    }

    public boolean isAdmin(Long userId) {
        Long num = userMapper.findUserRole(userId);
        return num == 1L;
    }

    public Optional<EmailVerify> sendVerifyCode(EmailVerify emailVerify) {
        User user = userMapper.findUserByEmail(emailVerify.getEmail());
        if (user == null) {
            return Optional.empty();
        }

        EmailVerify verify = emailService.sendVerificationEmail(user.getEmail());

        return Optional.of(verify);
    }
    
    public Optional<EmailVerify> registerCode(EmailVerify emailVerify) {

        EmailVerify verify = emailService.sendVerificationEmail(emailVerify.getEmail());

        return Optional.of(verify);
    }

    public void logoutUser() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        long userId = id.longValue();
        String redisKey = "token_" + userId;
        long expire = TimeUnit.HOURS.toMillis(7);
        stringRedisTemplate.opsForValue().set("token:"+redisKey, "", expire, TimeUnit.MILLISECONDS);
        userMapper.changeStatus(userId, 0);
    }

    public void cancelUser() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        Long userId = id.longValue();
        userMapper.cancelUser(userId);
    }

    public void updateUserExp(Integer expAdd) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        Long userId = id.longValue();
        Integer exp = userMapper.selectUserExp(userId);
        Integer expValue = exp + expAdd;
        userMapper.updateUserExp(expValue, userId);
    }
}
