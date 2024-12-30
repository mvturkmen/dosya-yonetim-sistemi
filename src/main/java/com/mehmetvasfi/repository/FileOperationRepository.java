package com.mehmetvasfi.repository;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Repository
public class FileOperationRepository {

    public List<String> getLogsByCategory(String category) {
        Path logFile = Paths.get("logs", category + ".txt");
        try {
            if (Files.exists(logFile)) {
                return Files.readAllLines(logFile);
            } else {
                return Collections.emptyList();
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
