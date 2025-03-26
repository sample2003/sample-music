package com.sample.music.annotation;

import org.redisson.api.RateIntervalUnit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD}) // 该注解可以标记在类或方法上
@Retention(RetentionPolicy.RUNTIME) // 注解在运行时保留，可通过反射获取
public @interface RateLimit {
    String key() default "default:rate:limit"; // 存储在redis的键名
    long rate() default 100; // 单位时间内的允许请求数
    long rateInterval() default 1; // 时间长度
    RateIntervalUnit unit() default RateIntervalUnit.MINUTES; // 时间单位
}
