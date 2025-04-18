package com.sample.music.service;

import com.sample.music.common.Result;
import com.sample.music.exception.BusinessException;
import com.sample.music.mapper.UserMapper;
import com.sample.music.pojo.dto.EmailVerify;
import com.sample.music.pojo.dto.UserDTO;
import com.sample.music.pojo.entity.User;
import com.sample.music.pojo.dto.UserRegister;
import com.sample.music.pojo.vo.UserUpdateVO;
import com.sample.music.utils.JwtUtil;
import com.sample.music.utils.Md5Util;
import com.sample.music.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.sample.music.constant.HttpStatusCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtUtil jwtUtil;

    private final UserMapper userMapper;

    private final EmailService emailService;

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 用户登录
     */
    public String loginUser(String text, String password) {
        User user = userMapper.findUserByUserName(text);
        if(user == null){
            user = userMapper.findUserByEmail(text);
        }
        if (user == null) {
            throw new BusinessException(NOT_FOUND, "用户未注册");
        }

        Long userId = user.getId();

        ValueOperations<String, String> operation = stringRedisTemplate.opsForValue();
        // token_
        String redisKey = "token_" + userId;

        String barredToken = operation.get("jwt:blacklist:" + redisKey);
        if (barredToken != null) {
            throw new BusinessException(FORBIDDEN, "该用户被封禁");
        }

        if (Md5Util.checkPassword(password, user.getPassword())) {

            String token = jwtUtil.genToken(String.valueOf(userId));
            // 把token存入redis
            operation.set("token:" + redisKey, token, 7, TimeUnit.DAYS);
            userMapper.changeStatus(userId, 0);
            return token;
        }
        throw new BusinessException(UNAUTHORIZED, "密码错误");
    }

    /**
     * 用户注册
     */
    @Transactional
    public Long registerUser(UserRegister user) {
        String email = user.getEmail();
        String password = user.getPassword();
        String verificationCode = user.getVerificationCode();
        String invitationCode = user.getInvitationCode();
        if (user.equals(new UserRegister())) throw new BusinessException(NOT_FOUND, "未填写注册信息");
        if (findUserByEmail(user.getEmail()) != null) throw new BusinessException(UNAUTHORIZED, "该邮箱已被注册");
        // 验证验证码或邀请码是否存在
        boolean isValidCode = verificationCode != null && emailService.validateVerifyCode(email, verificationCode);
        boolean hasInvitationCode = invitationCode.equals("cqt123");

        // 如果没有有效的验证码或邀请码，抛出异常
        if (!isValidCode || !hasInvitationCode) {
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
        Long userId = userInfo.getId();

        userMapper.addUserConfig(userId, 2);

        return id;
    }

    /**
     * 更新个人信息
     */
    public void updateUser(UserUpdateVO user) {
        Long userId = UserContext.getUser();
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
        Integer num = userMapper.findUserRole(userId);
        return num.equals(1);
    }

    public Optional<String> sendVerifyCode(EmailVerify emailVerify) {
        User user = userMapper.findUserByEmail(emailVerify.getEmail());
        if (user == null) {
            return Optional.empty();
        }

        EmailVerify verify = emailService.sendVerificationEmail(user.getEmail());

        return Optional.of("发送验证码成功");
    }

    public void logoutUser() {
        Long userId = UserContext.getUser();
        String redisKey = "token_" + userId;
        long expire = TimeUnit.HOURS.toMillis(7);
        stringRedisTemplate.opsForValue().set("token:"+redisKey, "", expire, TimeUnit.MILLISECONDS);
        userMapper.changeStatus(userId, 0);
    }

    public void cancelUser() {
        Long userId = UserContext.getUser();
        userMapper.cancelUser(userId);
    }

    public void updateUserExp(Integer expAdd) {
        Long userId = UserContext.getUser();
        Integer exp = userMapper.selectUserExp(userId);
        Integer expValue = exp + expAdd;
        userMapper.updateUserExp(expValue, userId);
    }

    /**
     * 根据用户名搜寻用户
     */
    public User findUserByName(String username) {
        return userMapper.findUserByUserName(username);
    }

    /**
     * 根据邮箱搜索
     */
    public User findUserByEmail(String email) {
        return userMapper.findUserByEmail(email);
    }

    /**
     * 如果用户名搜索不到就根据邮箱搜索
     */
    public User findUserByNameOrEmail(String text) {
        User user = userMapper.findUserByUserName(text);
        if(user == null){
            user = userMapper.findUserByEmail(text);
        }
        return user;
    }

    /**
     * 根据id查询用户（用户）
     */
    public UserDTO findUserById(Long id) {
        UserDTO user = userMapper.findUserById(id);
        Integer roleInt = userMapper.findUserRole(id);
        String role = "";
        if(roleInt.equals(0)) {
            role = "master";
        }else if(roleInt.equals(1)){
            role = "admin";
        }else if(roleInt.equals(2)){
            role = "user";
        }
        user.setRole(role);
        return user;
    }
}
