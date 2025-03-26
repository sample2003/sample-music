package com.sample.music.common;

import com.sample.music.annotation.RateLimit;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {
    private final RedissonClient redissonClient;

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        RRateLimiter limiter = redissonClient.getRateLimiter(rateLimit.key());
        limiter.trySetRate(
                RateType.OVERALL,
                rateLimit.rate(),
                rateLimit.rateInterval(),
                rateLimit.unit()
        );
        if (!limiter.tryAcquire(1)) {
            throw new RuntimeException("请求频率过高！");
        }
        return joinPoint.proceed();
    }
}
