package com.sample.music.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class Py {
    @Value("classpath*:py/parse_music_net.py")
    private String pythonScriptPath;

    public String[] callPythonScript(String songUrl) {
        try {
            // 构建Python脚本命令
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath, songUrl);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                String[] metadata = output.toString().split("\n");
                return metadata;
            } else {
                throw new RuntimeException("Python script failed with exit code " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing Python script", e);
        }
    }
}
