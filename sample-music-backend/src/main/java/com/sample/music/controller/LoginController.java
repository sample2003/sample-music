package com.sample.music.controller;

import cn.hutool.core.lang.UUID;
import com.sample.music.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class LoginController {

    private final RedisTemplate<String, Object> redisTemplate;

    private final static String WX_APP_ID = "a";

    private final static String WX_REDIRECT_URI = "";

    @GetMapping("/qrcode")
    public Result<String> generateQrcode() {
        String state = UUID.randomUUID().toString();
        String redirectUrl = "";

        /*if ("wechat".equals(type)) {
            redirectUrl = String.format(
                    "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s",
                    WX_APP_ID, URLEncoder.encode(WX_REDIRECT_URI), state);
        } else if ("qq".equals(type)) {
            redirectUrl = String.format(
                    "https://graph.qq.com/oauth2.0/show?which=Login&display=pc&response_type=code&client_id=%s&redirect_uri=%s&state=%s",
                    QQ_CLIENT_ID, URLEncoder.encode(QQ_REDIRECT_URI), state);
        }*/

        redirectUrl = String.format(
                "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s",
                WX_APP_ID, URLEncoder.encode(WX_REDIRECT_URI), state);

        // 存储state到Redis（有效期300秒）
        redisTemplate.opsForValue().set("qrcode:"+state, "unscanned", 300, TimeUnit.SECONDS);
        return Result.success(redirectUrl);
    }

    /*// 微信回调处理
    @GetMapping("/callback/wechat")
    public String wechatCallback(@RequestParam String code,
                                 @RequestParam String state) {
        // 验证state有效性
        if (Boolean.FALSE.equals(redisTemplate.hasKey("qrcode:" + state))) {
            return "无效请求";
        }

        // 获取access_token
        WxAccessToken token = wxAuthService.getAccessToken(code);

        // 获取用户信息
        WxUserInfo userInfo = wxAuthService.getUserInfo(token.getAccessToken(),
                token.getOpenid());

        // 处理用户数据
        processUser(userInfo, state);
        return "登录成功，请返回页面";
    }

    // QQ回调处理类似
    private void processUser(WxUserInfo userInfo, String state) {
        // 查询是否已存在用户
        User user = userRepository.findByOpenidWechat(userInfo.getOpenid());

        if (user == null) {
            user = new User();
            user.setOpenidWechat(userInfo.getOpenid());
            user.setUnionid(userInfo.getUnionid());
            user.setAvatar(userInfo.getHeadimgurl());
            userRepository.save(user);
        }

        // 生成系统Token
        String jwtToken = JwtUtils.generateToken(user.getId());

        // 更新Redis状态
        redisTemplate.opsForValue().set("qrcode:"+state, jwtToken, 300, TimeUnit.SECONDS);
    }*/

}
