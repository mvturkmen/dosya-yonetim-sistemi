package com.mehmetvasfi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;

@Service
public class BackupService {

    public void backupFile(Path source, Path backupDir) {
        try {
            Path target = backupDir.resolve(source.getFileName());
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            logBackup(source, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logBackup(Path source, Path target) {
        // Loglama mantığını burada yap
        System.out.println("Backup created: " + source + " -> " + target);
    }
}
