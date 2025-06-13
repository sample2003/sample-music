package com.sample.music;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@MapperScan({"com.sample.music.mapper", "com.sample.music.chat", "com.sample.music.recommend.mapper"})
@EnableCaching
@SpringBootApplication(scanBasePackages = {"com.sample.music", "com.sample.music.chat"})
public class SampleMusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleMusicApplication.class, args);
    }

}
