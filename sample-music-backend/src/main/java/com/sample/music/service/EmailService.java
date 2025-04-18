package com.sample.music.service;

import com.sample.music.pojo.dto.EmailVerify;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String username;

    private final JavaMailSender javaMailSender;

    private final StringRedisTemplate stringRedisTemplate;

    /** 发送验证码
     * @param email 邮箱
     * @return EmailVerify
     */
    public EmailVerify sendVerificationEmail(String email) {
        // 生成验证码并填入EmailVerify
        String code = this.generateVerificationCode();
        EmailVerify emailVerify = new EmailVerify();
        emailVerify.setEmail(email);
        emailVerify.setCode(code);

        // 给用户的验证码消息
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(username);
        mail.setTo(email);
        mail.setSubject("样本音乐验证码");
        mail.setText("你的验证码为: " + code);

        // 保存验证码到redis
        this.saveVerifyCode(emailVerify);

        // 发送验证码
        javaMailSender.send(mail);
        return emailVerify;
    }

    /**
     * 生成验证码
     * @return String
     */
    private String generateVerificationCode() {
        // 生成一个随机的验证码
        return RandomStringUtils.randomNumeric(6);
    }

    /**
     * 将验证码存入redis
     * @param emailVerify 邮箱与验证码
     */
    private void saveVerifyCode(EmailVerify emailVerify){
        String code = emailVerify.getCode();
        String email = emailVerify.getEmail();
        // 过期时间：1分钟
        long expire = TimeUnit.MINUTES.toMillis(1);
        // 存入redis：verify/email
        stringRedisTemplate.opsForValue().set("verify:"+email, code, expire, TimeUnit.MILLISECONDS);
    }

    /** 验证验证码
     * @param email 邮箱
     * @return boolean
     */
    public boolean validateVerifyCode(String email, String submittedCode) {
        String storedCode = stringRedisTemplate.opsForValue().get("verify:"+email);

        // 返回验证码是否正确
        return storedCode != null && storedCode.equals(submittedCode);
    }

    /**
     * 删除验证码
     * @param email 邮箱
     */
    public void deleteVerifyCode(String email){
        stringRedisTemplate.delete(email);
    }
}
