package com.sample.music.controller;

import com.sample.music.utils.Py;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class SongControllerTest {

    @Test
    void parseSongMetadata() throws IOException {
        String l = "lyric";
        Path lyricsPath = Paths.get("..\\lyrics", l + ".txt");
        System.out.println(lyricsPath.getParent());
        Files.createDirectories(lyricsPath.getParent()); // 创建目录
    }
}