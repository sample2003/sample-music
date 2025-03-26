package com.sample.music;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SampleMusicApplicationTests {

    @Test
    void contextLoads() {
        String accessKeyId = System.getenv("OSS_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("OSS_ACCESS_KEY_SECRET");
        System.out.println(accessKeySecret + accessKeyId);
    }

}
