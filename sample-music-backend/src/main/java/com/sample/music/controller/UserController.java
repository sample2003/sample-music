package com.sample.music.controller;

import cn.hutool.core.lang.UUID;
import com.sample.music.annotation.RateLimit;
import com.sample.music.pojo.dto.EmailVerify;
import com.sample.music.common.FilesType;
import com.sample.music.pojo.entity.User;
import com.sample.music.common.Result;
import com.sample.music.pojo.vo.UserRegisterVO;
import com.sample.music.pojo.vo.UserUpdateVO;
import com.sample.music.mapper.UserMapper;
import com.sample.music.pojo.vo.UserVO;
import com.sample.music.service.UserService;
import com.sample.music.service.EmailService;
import com.sample.music.service.FileManageService;
import com.sample.music.utils.JwtUtil;
import com.sample.music.utils.Md5Util;
import com.sample.music.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.sample.music.constant.HttpStatusCode.*;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final FileManageService fileManageService;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户注册
     *
     * @param userRegisterVO 注册用户信息
     * @return Result
     */
    @PostMapping("register")
    public Result<String> registerUser(UserRegisterVO userRegisterVO) {
        if (userRegisterVO == null) {
            return Result.error("请填写相关信息");
        }
        if (userService.findUserByName(userRegisterVO.getUsername()) != null) return Result.error("该用户名已被注册");
        if (userService.findUserByEmail(userRegisterVO.getEmail()) != null) return Result.error("该邮箱已被注册");
        Long id = userService.registerUser(userRegisterVO);
        return Result.success(id + "注册成功");
    }

    /**
     * 用户登录
     *
     * @param text     用户名或邮箱
     * @param password 密码
     * @return Result
     */
    @RateLimit
    @PostMapping("login")
    public Result<String> loginUser(
            @Pattern(regexp = ".*") String text,
            @Pattern(regexp = ".*") String password
    ) {
        if (text == null || password == null) {
            return Result.error(BAD_REQUEST, "请填写完整登录信息");
        }
        User loginUser = userService.findUserByNameOrEmail(text);
        if (loginUser == null) {
            return Result.error(NOT_FOUND, "用户未注册");
        }

        ValueOperations<String, String> operation = stringRedisTemplate.opsForValue();
        // token_
        String redisKey = "token_" + loginUser.getId();

        String barredToken = operation.get("jwt:blacklist:" + redisKey);
        if (barredToken != null) {
            return Result.error(FORBIDDEN, "该用户被封禁");
        }
        if (Md5Util.checkPassword(password, loginUser.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = jwtUtil.genToken(claims);
            // 把token存入redis
            operation.set("token:" + redisKey, token, 7, TimeUnit.DAYS);
            userMapper.changeStatus(loginUser.getId(), 0);
            return Result.success(OK, "登录成功", token);
        }
        return Result.error(UNAUTHORIZED, "密码错误");
    }

    /**
     * 用户登出
     *
     * @return Result
     */
    @GetMapping("logout")
    public Result<?> logoutUser() {
        userService.logoutUser();
        return Result.success();
    }

    /**
     * 用户注销
     *
     * @return Result<?>
     */
    @GetMapping("cancel")
    public Result<?> cancelUser() {
        userService.cancelUser();
        return Result.success();
    }

    /**
     * 用户查询个人信息
     *
     * @return Result<User>
     */
    @GetMapping("info")
    public Result<UserVO> userInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");

        User user = userService.findUserByNameOrEmail(username);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        if (userService.isAdmin(user.getId())) {
            userVO.setRole("admin");
        } else {
            userVO.setRole("user");
        }
        return Result.success(OK, "查询成功", userVO);
    }

    /**
     * 用户更新个人信息
     *
     * @param user 用户信息
     * @return Result
     */
    @PutMapping("updateUser")
    public Result<String> updateUser(@RequestBody UserUpdateVO user) {
        user.setUpdateTime(LocalDateTime.now());
        userService.updateUser(user);
        return Result.success("用户信息修改成功");
    }

    /**
     * 发送验证码
     *
     * @param emailVerify 邮箱和验证码
     * @return Result
     */
    @PostMapping("sendVerifyCode")
    public Result<?> sendVerifyCode(@RequestBody EmailVerify emailVerify) {
        return userService.registerCode(emailVerify)
                .map(Result::success)
                .orElseGet(() -> Result.error("发送验证码失败"));
    }

    @GetMapping("/qrcode")
    public Result<String> generateQrcode(@RequestParam String type) {
        String state = UUID.randomUUID().toString();
        String redirectUrl = "";

/*        if ("wechat".equals(type)) {
            redirectUrl = String.format(
                    "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s",
                    WX_APP_ID, URLEncoder.encode(WX_REDIRECT_URI), state);
        } else if ("qq".equals(type)) {
            redirectUrl = String.format(
                    "https://graph.qq.com/oauth2.0/show?which=Login&display=pc&response_type=code&client_id=%s&redirect_uri=%s&state=%s",
                    QQ_CLIENT_ID, URLEncoder.encode(QQ_REDIRECT_URI), state);
        }*/

        // 存储state到Redis（有效期300秒）
        redisTemplate.opsForValue().set("qrcode:"+state, "unscathed", 300, TimeUnit.SECONDS);
        return Result.success(redirectUrl);
    }

    /**
     * 用户更新密码
     *
     * @param email       邮箱
     * @param code        验证码
     * @param newPassword 新密码
     * @return Result
     */
    @PatchMapping("updateUserPwd")
    public Result<?> updateUserPwd(String email, String code, String newPassword) {

        if (email == null) {
            return Result.error("邮箱不可为空");
        } else if (code == null) {
            return Result.error("验证码为空");
        } else if (newPassword == null) {
            return Result.error("密码不可为空");
        }
        if (userService.findUserByNameOrEmail(email) != null) {
            if (emailService.validateVerifyCode(email, code)) {
                userService.updatePwd(newPassword, email);
                emailService.deleteVerifyCode(email);
                return Result.success("密码重置成功");
            }
        }
        return Result.error("密码重置失败");
    }

    /**
     * 用户更新头像
     *
     * @param image 头像文件
     * @return Result
     */
    @PatchMapping("updateUserAvatar")
    public Result<?> updateUserAvatar(FilesType image) {
        if (image == null) {
            return Result.error("提交失败");
        }
        fileManageService.uploadFile(image.getImageFiles(), "cover");
        return Result.success("更改成功");
    }

    /**
     * 用户经验增加
     *
     * @param expAdd 经验值
     * @return Result
     */
    @PatchMapping("updateUserExp")
    public Result<?> updateUserExp(Integer expAdd) {
        if (expAdd == 0) {
            return Result.error("经验为0");
        }
        userService.updateUserExp(expAdd);
        return Result.success("新增经验：" + expAdd);
    }
}
