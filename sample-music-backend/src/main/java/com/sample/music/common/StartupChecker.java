package com.sample.music.common;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class StartupChecker {

    private final JdbcTemplate jdbcTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    public StartupChecker(JdbcTemplate jdbcTemplate, RedisTemplate<String, String> redisTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisTemplate = redisTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void checkServices() {
        checkMySQL();
        checkRedis();
    }

    private void checkMySQL() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            logStatus("\nMySQL  \u2705  Connected", true); // ✅
        } catch (Exception e) {
            logStatus("\nMySQL  \u274C  Connection Failed: " + e.getMessage(), false); // ❌
        }
    }

    private void checkRedis() {
        try {
            redisTemplate.getConnectionFactory().getConnection().ping();
            logStatus("Redis  \u2705  Connected", true); // ✅
        } catch (Exception e) {
            logStatus("Redis  \u274C  Connection Failed: " + e.getMessage(), false); // ❌
        }
    }

    private void logStatus(String message, boolean success) {
        String color = success ? "\u001B[32m" : "\u001B[31m"; // 绿色成功，红色失败
        System.out.println(color + message + "\u001B[0m"); // 重置颜色
    }
}