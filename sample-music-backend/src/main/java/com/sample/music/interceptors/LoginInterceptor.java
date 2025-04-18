package com.sample.music.interceptors;

import com.sample.music.annotation.Public;
import com.sample.music.exception.BusinessException;
import com.sample.music.utils.JwtUtil;
import com.sample.music.utils.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.sample.music.constant.HttpStatusCode.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate stringRedisTemplate;

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            // 注解 @Public，开放接口
            boolean methodHasPublic = handlerMethod.getMethodAnnotation(Public.class) != null;
            boolean classHasPublic = handlerMethod.getBeanType().isAnnotationPresent(Public.class);
            if (methodHasPublic || classHasPublic) {
                return true;
            }
        }
        // 令牌验证
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        // 获取token，若没有则抛出未登录异常
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.length() < 8) {
            // 如果Authorization头部不存在或者值太短，抛出未登录异常
            throw new BusinessException(UNAUTHORIZED, "未登录");
        }
        String token = authHeader.substring(7);

        // 验证token，从请求中获取用户ID
        Long userId = jwtUtil.parseToken(token);
        if (userId == null ) {
            // 如果无法解析令牌或令牌中不包含用户ID，返回401错误
            throw new BusinessException(UNAUTHORIZED, "未登录");
        }

        // 构造Redis中的键名
        String redisKey = "token_" + userId;

        // 从redis中获取相同的token
        ValueOperations<String, String> operation = stringRedisTemplate.opsForValue();
        String redisToken = operation.get("token:" + redisKey);

        // 验证从Redis中获取的token是否与请求中的token相同
        if (token.equals(redisToken)) {
            // 令牌匹配，放行
            // 把业务数据存储到ThreadLocal中
            UserContext.setUser(userId);
            return true;
        } else {
            // 令牌不匹配，返回401错误
            throw new BusinessException(UNAUTHORIZED, "未登录");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除ThreadLocal中的数据
        UserContext.removeUser();
    }
}
