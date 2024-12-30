package com.mehmetvasfi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.stereotype.Service;

@Service
public class LoggingService {

    public void logToFile(String category, String content) {
        try {
            Path logPath = Paths.get("logs", category + ".txt");
            Files.createDirectories(logPath.getParent());
            Files.write(logPath, (content + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
