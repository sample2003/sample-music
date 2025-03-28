package com.sample.music.controller;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class SongControllerTest {

    @Test
    void parseSongMetadata() throws IOException {
        String l = "lyric";
        Path lyricsPath = Paths.get("..\\lyrics", l + ".txt");
        System.out.println(lyricsPath.getParent());
        Files.createDirectories(lyricsPath.getParent()); // 创建目录
    }
}