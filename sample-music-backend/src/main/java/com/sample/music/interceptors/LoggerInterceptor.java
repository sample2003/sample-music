package com.sample.music.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.music.mapper.UserMapper;
import com.sample.music.pojo.entity.RequestLog;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LoggerInterceptor implements HandlerInterceptor {
    private static final ThreadLocal<Long> startTimeHolder = new ThreadLocal<>();
    private final UserMapper userMapper;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        startTimeHolder.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        try {
            long executionTime = System.currentTimeMillis() - startTimeHolder.get();
            RequestLog log = new RequestLog();
            log.setRequestMethod(request.getMethod());
            log.setUrl(request.getRequestURI());
//            log.setRemoteAddr(request.getRemoteAddr());
//            log.setHttpStatus(response.getStatus());
//            log.setResponse(".");
            log.setConsume(new BigDecimal(String.valueOf(executionTime)).setScale(0, RoundingMode.HALF_UP));
            log.setRequestTime(String.valueOf(LocalDateTime.now()));

            // 记录请求参数（过滤敏感信息）
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                log.setRequest(objectMapper.writeValueAsString(request.getParameterMap()));
            }

            userMapper.insertLogger(log);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }finally {
            startTimeHolder.remove();
        }
    }
}
