package com.sample.music.service;

import com.sample.music.exception.BusinessException;
import com.sample.music.mapper.UserMapper;
import com.sample.music.pojo.entity.User;
import com.sample.music.pojo.vo.UserRegisterVO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.sample.music.constant.HttpStatusCode.UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserMapper userMapper;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_WhenEmailExists_ShouldThrowException() {
        // 准备测试数据
        UserRegisterVO vo = new UserRegisterVO();
        vo.setEmail("1381231231@qq.com");
        vo.setPassword("password123");
        vo.setVerificationCode("123456");
        vo.setInvitationCode("cqt123");

        // 配置 Mock 行为
        when(userMapper.findUserByEmail("1381231231@qq.com"))
                .thenReturn(new User()); // 模拟已存在用户

        // 执行并验证异常
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.registerUser(vo));

        // 断言验证
        assertEquals("该邮箱已被注册", exception.getMessage());
        assertEquals(UNAUTHORIZED, exception.getCode());
    }
}