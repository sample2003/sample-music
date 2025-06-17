package com.sample.music;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication(scanBasePackages = {"com.sample.music", "com.sample.music.chat"})
@MapperScan({"com.sample.music.mapper", "com.sample.music.chat", "com.sample.music.recommend.mapper"})
public class SampleMusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleMusicApplication.class, args);
    }

}
